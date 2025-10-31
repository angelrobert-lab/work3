package com.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.domain.enums.FriendStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 好友申请表持久化对象
 */
@Data
@Builder
@TableName("friend_apply") // 数据库表名
@Schema(description = "好友申请表持久化对象（存储用户间的好友申请记录）")
public class FriendApplyPo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "申请记录ID（自增主键）")
    private Long id;

    @TableField("apply_user_id")
    @Schema(description = "申请人用户ID")
    private Long applyUserId;

    @TableField("target_user_id")
    @Schema(description = "目标用户ID")
    private String targetUserId;

    @TableField(value = "apply_time", fill = FieldFill.INSERT)
    @Schema(description = "申请发起时间")
    private LocalDateTime applyTime;

    @Schema(description = "申请状态（0-未处理，1-已同意，2-已拒绝）")
    private FriendStatusEnum status;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "状态更新时间")
    private LocalDateTime updateTime;

}