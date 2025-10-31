package com.controller;

import com.annotation.APPLoginUser;
import com.common.enums.BusinessCodeEnum;
import com.domain.dto.CommentSubmitDto;
import com.domain.dto.TokenDto;
import com.domain.query.PageQuery;
import com.domain.vo.CommentsVo;
import com.domain.vo.PageVo;
import com.service.ICommentsService;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Tag(name = "评论管理", description = "多级评论相关接口")
public class CommentsController {

    @Autowired
    private ICommentsService commentsService;

    @Operation(summary = "发表评论")
    @PostMapping
    public ResponseResult<CommentsVo> addComment(
            @Valid @RequestBody CommentSubmitDto commentSubmitDto,
            @APPLoginUser TokenDto tokenDto
    ) {
        CommentsVo comment = commentsService.addComment(commentSubmitDto, tokenDto.getUserId());
        return ResponseResult.success(BusinessCodeEnum.ADD_COMMENT_SUCCESS,comment);
    }

    @Operation(summary = "查询文章评论列表")
    @GetMapping("/article/{articleId}")
    public ResponseResult<PageVo<CommentsVo>> getArticleComments(
            @PathVariable Integer articleId,
            @Valid PageQuery pageQuery
    ) {
        PageVo<CommentsVo> pageVo = commentsService.getArticleComments(articleId, pageQuery);
        return ResponseResult.success(BusinessCodeEnum.QUERY_COMMENT_SUCCESS,pageVo);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{commentId}")
    public ResponseResult<Void> deleteComment(
            @PathVariable Integer commentId,
            @APPLoginUser TokenDto tokenDto
    ) {
        commentsService.deleteComment(commentId, tokenDto.getUserId());
        return ResponseResult.success();
    }
}