package com.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "评论展示VO")
public class CommentsVo {

    @Schema(description = "评论ID")
    private Integer id;

    @Schema(description = "评论用户昵称")
    private String username;

    @Schema(description = "评论用户ID")
    private Long userId;

    @Schema(description = "评论内容")
    private String message;

    @Schema(description = "父评论ID")
    private Integer parentId;

    @Schema(description = "被回复的用户昵称")
    private String parentName;

    @Schema(description = "根评论ID")
    private Integer originId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @Schema(description = "子评论列表")
    private List<CommentsVo> children;
}