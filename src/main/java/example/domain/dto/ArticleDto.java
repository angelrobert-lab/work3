package com.domain.dto;

import com.domain.enums.ArticleVisibilityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "文章创建/更新请求参数")
public class ArticleDto {
    private static final long serialVersionUID = 1L;

    @Schema(description = "文章封面图片URL")
    private String coverImage;

    @NotBlank(message = "文章标题不能为空")
    @Schema(description = "文章题目", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "文章简介")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    @Schema(description = "文章文本内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "可见性状态（0-仅自己可见，1-公开，2-仅好友可见）",
            defaultValue = "1",
            allowableValues = {"0", "1", "2"})
    private ArticleVisibilityEnum visibility = ArticleVisibilityEnum.PUBLIC;
}