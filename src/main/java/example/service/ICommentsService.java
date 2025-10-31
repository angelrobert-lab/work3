package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.dto.CommentSubmitDto;
import com.domain.po.CommentsPo;
import com.domain.query.PageQuery;
import com.domain.vo.CommentsVo;
import com.domain.vo.PageVo;

public interface ICommentsService extends IService<CommentsPo> {
    CommentsVo addComment(CommentSubmitDto dto, Long userId);
    PageVo<CommentsVo> getArticleComments(Integer articleId, PageQuery pageQuery);
    void deleteComment(Integer commentId, Long userId);
}