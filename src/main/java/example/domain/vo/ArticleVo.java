package com.domain.vo;

import com.domain.enums.ArticleVisibilityEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章展示VO")
public class ArticleVo {
    @Schema(description = "文章ID")
    private Integer id;

    @Schema(description = "文章封面图片URL")
    private String coverImage;

    @Schema(description = "文章题目")
    private String title;

    @Schema(description = "文章简介")
    private String summary;

    @Schema(description = "文章文本内容")
    private String content;

    @Schema(description = "可见性状态")
    private ArticleVisibilityEnum visibility;

    @Schema(description = "作者昵称")
    private String authorName;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}