package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.domain.po.FriendApplyPo;
import org.apache.ibatis.annotations.Param;

/**
 * 好友申请表 Mapper 接口
 * 负责好友申请相关的数据查询与操作
 */
public interface FriendApplyMapper extends BaseMapper<FriendApplyPo> {

    /**
     * 分页查询当前用户的好友申请列表
     * @param page 分页参数（页码、每页条数）
     * @param targetUserId 当前用户ID（申请接收方）
     * @return 分页后的好友申请列表
     */
    IPage<FriendApplyPo> selectMyFriendApplyPage(
            Page<FriendApplyPo> page,
            @Param("targetUserId") Long targetUserId
    );

    /**
     * 查询指定双方的未处理申请
     * @param applyUserId 申请发起方ID
     * @param targetUserId 申请接收方ID
     * @return 未处理的好友申请记录（null 表示无）
     */
    FriendApplyPo selectPendingApply(
            @Param("applyUserId") Long applyUserId,
            @Param("targetUserId") Long targetUserId
    );
}