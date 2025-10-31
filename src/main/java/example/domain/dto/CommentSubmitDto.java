package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "评论提交请求参数")
public class CommentSubmitDto {

    @NotNull(message = "文章ID不能为空")
    @Schema(description = "所属文章ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer articleId;

    @NotBlank(message = "评论内容不能为空")
    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "父评论ID（一级评论为0）",defaultValue = "0")
    private Integer parentId = 0;

    @Schema(description = "根评论ID（一级评论为0，回复评论时填写对应根评论ID）", defaultValue = "0")
    private Integer originId = 0;
}