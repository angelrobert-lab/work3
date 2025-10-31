package com.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.domain.enums.FriendStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("friend_relation")
public class FriendRelationPo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "好友ID")
    @TableField("friend_id")
    private Long friendId;

    @Schema(description = "关系状态：0-待验证 1-已成为好友 2-已拒绝")
    private FriendStatusEnum status;

    @Schema(description = "申请时间")
    @TableField("apply_time")
    private LocalDateTime applyTime;

    @Schema(description = "通过时间")
    @TableField("confirm_time")
    private LocalDateTime confirmTime;

    @Schema(description = "备注名")
    private String remark;
}