package com.controller;

import com.annotation.APPLoginUser;
import com.common.enums.BusinessCodeEnum;
import com.domain.dto.ArticleMetricUpdateDto;
import com.domain.dto.TokenDto;
import com.domain.vo.ArticleMetricVo;
import com.service.IArticleMetricService;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article/{articleId}/metrics")
@RequiredArgsConstructor
public class ArticleMetricController {

    private final IArticleMetricService articleMetricService;

    @Operation(summary = "获取文章互动指标")
    @GetMapping
    public ResponseResult<ArticleMetricVo> getMetrics(@PathVariable Integer articleId, HttpServletRequest request) {
        TokenDto tokenDto = (TokenDto) request.getAttribute("tokenDto");
        Long userId = tokenDto != null ? tokenDto.getUserId() : null;
        ArticleMetricVo metricVo = articleMetricService.getMetrics(articleId, userId);
        return ResponseResult.success(BusinessCodeEnum.ARTICLE_METRIC_QUERY_SUCCESS, metricVo);
    }

    @Operation(summary = "点赞/取消点赞")
    @PostMapping("/like")
    public ResponseResult<ArticleMetricVo> toggleLike(@PathVariable Integer articleId, @APPLoginUser TokenDto tokenDto) {
        ArticleMetricVo metricVo = articleMetricService.toggleLike(articleId, tokenDto.getUserId());
        return ResponseResult.success(BusinessCodeEnum.ARTICLE_LIKE_STATUS_SUCCESS, metricVo);
    }

    @Operation(summary = "更新评论数/收藏数")
    @PutMapping
    public ResponseResult<ArticleMetricVo> updateCounters(@PathVariable Integer articleId,
                                                          @RequestBody ArticleMetricUpdateDto updateDto) {
        ArticleMetricVo metricVo = articleMetricService.updateBaseCounters(articleId, updateDto);
        return ResponseResult.success(BusinessCodeEnum.UPDATE_ARTICLE_METRIC_SUCCESS, metricVo);
    }
}
