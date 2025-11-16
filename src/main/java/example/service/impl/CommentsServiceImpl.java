package com.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.common.enums.BusinessCodeEnum;
import com.converter.CommentsConverter;
import com.domain.dto.CommentSubmitDto;
import com.domain.po.CommentsPo;
import com.domain.po.EIException;
import com.domain.po.UserPo;
import com.domain.query.PageQuery;
import com.domain.vo.CommentsVo;
import com.domain.vo.PageVo;
import com.mapper.CommentsMapper;
import com.service.IArticleMetricService;
import com.service.ICommentsService;
import com.utils.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, CommentsPo> implements ICommentsService {

    private final IArticleMetricService articleMetricService;
    /**
     * 添加评论
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentsVo addComment(CommentSubmitDto dto, Long userId) {
        UserPo user = Db.lambdaQuery(UserPo.class)
                .eq(UserPo::getId, userId).one();
        if (user == null) {
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        CommentsPo comment = CommentsConverter.INSTANCE
                .submitDtoToCommentsPo(dto, user.getId(), user.getUsername());

        if (dto.getParentId() == 0) {
            // 一级评论
            comment.setOriginId(0);
            comment.setParentName(null);
        } else {
            CommentsPo parent = getById(dto.getParentId());
            if (parent == null) {
                throw new EIException(BusinessCodeEnum.NOT_EXIT_FATHER_COMMENT);
            }
            comment.setOriginId(parent.getOriginId() == 0 ? parent.getId() : parent.getOriginId());
            comment.setParentName(parent.getUsername());
        }

        save(comment);

        // 如果是一级评论，则 originId 更新为自身 id
        if (dto.getParentId() == 0) {
            comment.setOriginId(comment.getId());
            updateById(comment);
        }

        articleMetricService.refreshCommentCount(dto.getArticleId());

        return CommentsConverter.INSTANCE.commentsPoToCommentsVo(comment);
    }

    /**
     * 获取文章评论列表（分页 + 嵌套子评论）
     */
    @Override
    public PageVo<CommentsVo> getArticleComments(Integer articleId, PageQuery pageQuery) {
        Query<CommentsPo> query = new Query<>(pageQuery);
        Page<CommentsPo> page = query.getPage();

        // 一级评论分页查询
        LambdaQueryWrapper<CommentsPo> firstWrapper = new LambdaQueryWrapper<CommentsPo>()
                .eq(CommentsPo::getArticleId, articleId)
                .eq(CommentsPo::getParentId, 0)
                .orderByAsc(CommentsPo::getCreatedAt);

        // 使用父类的 getBaseMapper() 获取具体 Mapper
        IPage<CommentsVo> firstPage = getBaseMapper().selectComment(page, firstWrapper);
        List<CommentsVo> roots = firstPage.getRecords();
        if (roots.isEmpty()) {
            return PageVo.of(Collections.emptyList(), 0L, (int) page.getCurrent(), (int) page.getSize());
        }

        // 批量查找所有子评论
        List<Integer> originIds = roots.stream().map(CommentsVo::getId).toList();
        LambdaQueryWrapper<CommentsPo> childWrapper = new LambdaQueryWrapper<CommentsPo>()
                .in(CommentsPo::getOriginId, originIds)
                .ne(CommentsPo::getParentId, 0)
                .orderByAsc(CommentsPo::getCreatedAt);

        // 使用父类的 getBaseMapper() 获取具体 Mapper
        List<CommentsVo> allChildren = getBaseMapper().selectCommentListView(childWrapper);
        Map<Integer, List<CommentsVo>> groupedByOrigin =
                allChildren.stream().collect(Collectors.groupingBy(CommentsVo::getOriginId));

        // 构建层级结构
        for (CommentsVo root : roots) {
            List<CommentsVo> children = groupedByOrigin.getOrDefault(root.getId(), Collections.emptyList());
            root.setChildren(buildNestedComments(root.getId(), children));
        }

        return PageVo.of(roots, firstPage.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }

    /**
     * 删除评论（支持级联删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer commentId, Long userId) {
        CommentsPo comment = getById(commentId);
        if (comment == null) {
            throw new EIException(BusinessCodeEnum.COMMENT_NOT_EXIST);
        }

        Integer articleId = comment.getArticleId();

        if (!comment.getUserId().equals(userId)) {
            throw new EIException(BusinessCodeEnum.NO_PERMISSION_TO_DELETE);
        }

        // 若为根评论：删除整棵树
        if (comment.getParentId() == 0) {
            LambdaQueryWrapper<CommentsPo> wrapper = new LambdaQueryWrapper<CommentsPo>()
                    .eq(CommentsPo::getOriginId, comment.getId())
                    .or()
                    .eq(CommentsPo::getId, comment.getId());
            remove(wrapper);
        } else {
            // 删除自身及子孙评论
            LambdaQueryWrapper<CommentsPo> wrapper = new LambdaQueryWrapper<CommentsPo>()
                    .eq(CommentsPo::getOriginId, comment.getOriginId());
            List<CommentsPo> all = list(wrapper);

            // 收集所有需删除的 ID（包括子孙节点）
            Set<Integer> toDelete = new HashSet<>();
            collectChildren(comment.getId(), all, toDelete);
            toDelete.add(comment.getId());
            removeBatchByIds(toDelete);
        }

        articleMetricService.refreshCommentCount(articleId);
    }

    private void collectChildren(Integer parentId, List<CommentsPo> all, Set<Integer> toDelete) {
        for (CommentsPo c : all) {
            if (Objects.equals(c.getParentId(), parentId)) {
                toDelete.add(c.getId());
                collectChildren(c.getId(), all, toDelete);
            }
        }
    }

    /**
     * 递归构建多层嵌套评论
     */
    private List<CommentsVo> buildNestedComments(Integer parentId, List<CommentsVo> comments) {
        return comments.stream()
                .filter(c -> Objects.equals(c.getParentId(), parentId))
                .map(c -> {
                    c.setChildren(buildNestedComments(c.getId(), comments));
                    return c;
                })
                .collect(Collectors.toList());
    }
}