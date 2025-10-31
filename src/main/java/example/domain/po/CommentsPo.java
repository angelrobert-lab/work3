package com.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


/**
 * <p>
 * 评论表
 * </p>
 *
 * @author author
 * @since 2025-10-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("comments")
@Schema(description="评论")
public class CommentsPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "评论ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "评论用户昵称")
    private String username;

    @Schema(description = "评论用户ID")
    private Long userId;

    @Schema(description = "所属文章ID")
    private Integer articleId;

    @Schema(description = "评论内容")
    private String message;

    @Schema(description = "父评论ID（用于回复功能）")
    private Integer parentId;

    @Schema(description = "父评论用户昵称")
    private String parentName;

    @Schema(description = "根评论ID")
    @TableField("origin_id")
    private Integer originId;

    @Schema(description = "评论唯一标识")
    private String uuid;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

}
