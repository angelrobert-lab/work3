package com.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.domain.po.CommentsPo;
import com.domain.vo.CommentsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CommentsMapper extends BaseMapper<CommentsPo> {

    // 基础查询：不分页，与 XML 中 selectCommentListView 对应
    List<CommentsVo> selectCommentListView(@Param(Constants.WRAPPER) Wrapper<CommentsPo> wrapper);

    // 分页查询：与 XML 中 selectComment 对应
    IPage<CommentsVo> selectComment(Page<CommentsPo> page, @Param(Constants.WRAPPER) Wrapper<CommentsPo> wrapper);
}