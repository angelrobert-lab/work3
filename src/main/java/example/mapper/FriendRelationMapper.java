package com.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.domain.po.FriendRelationPo;
import com.domain.vo.FriendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 好友关系表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2025-10-17
 */
public interface FriendRelationMapper extends BaseMapper<FriendRelationPo> {

    /**
     * 分页查询用户的好友列表（包含好友基本信息）
     * @param page 分页参数
     * @param wrapper 查询条件（需包含userId）
     * @return 分页的好友VO列表
     */
    IPage<FriendVo> selectFriendPage(Page<FriendRelationPo> page, @Param(Constants.WRAPPER) Wrapper<FriendRelationPo> wrapper);



    /**
     * 校验两个用户是否为好友
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 1-是好友 0-不是好友
     */
    Integer checkMutualFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);
}