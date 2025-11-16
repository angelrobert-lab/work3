package com.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章互动指标展示对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章互动指标VO")
public class ArticleMetricVo {

    @Schema(description = "文章ID")
    private Integer articleId;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "评论数")
    private Long commentCount;

    @Schema(description = "收藏数")
    private Long favoriteCount;

    @Schema(description = "当前用户是否点赞")
    private Boolean liked;
}
