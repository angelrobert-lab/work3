package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章互动指标更新请求
 */
@Data
@Schema(description = "文章互动指标更新参数")
public class ArticleMetricUpdateDto {

    @Schema(description = "最新评论总数，可为空表示不更新")
    private Long commentCount;

    @Schema(description = "最新收藏总数，可为空表示不更新")
    private Long favoriteCount;
}
