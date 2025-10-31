package com.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.domain.enums.ArticleVisibilityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 文章
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article")
@Schema(description="文章表")
public class ArticlePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文章ID（自增）")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "作者用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "文章封面图片URL")
    @TableField(value = "cover_image")
    private String coverImage;

    @Schema(description = "文章题目")
    private String title;

    @Schema(description = "文章简介")
    private String summary;

    @Schema(description = "文章文本内容")
    private String content;

    @Schema(description = "文章可见性状态")
    private ArticleVisibilityEnum visibility;

    @Schema(description = "创建时间")
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

}
