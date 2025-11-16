package com.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章互动指标表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_metrics")
@Schema(description = "文章互动指标")
public class ArticleMetricPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文章ID")
    @TableId(value = "article_id", type = IdType.INPUT)
    private Integer articleId;

    @Schema(description = "浏览量")
    @TableField("view_count")
    private Long viewCount;

    @Schema(description = "点赞数")
    @TableField("like_count")
    private Long likeCount;

    @Schema(description = "评论数")
    @TableField("comment_count")
    private Long commentCount;

    @Schema(description = "收藏数")
    @TableField("favorite_count")
    private Long favoriteCount;

    @Schema(description = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
