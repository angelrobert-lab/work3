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
 * 文章点赞记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_like")
@Schema(description = "文章点赞记录")
public class ArticleLikePo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "文章ID")
    @TableField("article_id")
    private Integer articleId;

    @Schema(description = "点赞用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "点赞时间")
    @TableField("created_at")
    private LocalDateTime createdAt;
}
