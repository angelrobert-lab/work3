package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.dto.FriendApplyDto;
import com.domain.dto.FriendDto;
import com.domain.dto.FriendOperateDto;
import com.domain.po.FriendRelationPo;
import com.domain.query.PageQuery;
import com.domain.vo.FriendApplyVo;
import com.domain.vo.FriendVo;
import com.domain.vo.PageVo;
import com.utils.ResponseResult;

import java.util.List;

public interface IFriendService extends IService<FriendRelationPo> {
    // 发送好友申请
    ResponseResult<Void> sendApply(FriendApplyDto dto, Long currentUserId);

    // 处理好友申请（同意/拒绝）
    ResponseResult<Void> handleApply(FriendOperateDto dto, Long currentUserId);

    // 获取好友列表
    ResponseResult<PageVo<FriendVo>> getFriendList(Long userId, PageQuery pageQuery);

    // 校验是否为好友
    boolean isMutualFriend(Long userId, Long targetUserId);

    // 删除好友
    ResponseResult<Void> deleteFriend(Long friendId, Long currentUserId);

    ResponseResult<PageVo<FriendApplyVo>> getApplyList(Long userId, PageQuery pageQuery);
}
