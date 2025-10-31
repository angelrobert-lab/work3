package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.enums.BusinessCodeEnum;
import com.converter.FriendConverter;
import com.domain.dto.FriendApplyDto;
import com.domain.dto.FriendDto;
import com.domain.dto.FriendOperateDto;
import com.domain.enums.FriendStatusEnum;
import com.domain.po.EIException;
import com.domain.po.FriendApplyPo;
import com.domain.po.FriendRelationPo;
import com.domain.po.UserPo;
import com.domain.query.PageQuery;
import com.domain.vo.FriendApplyVo;
import com.domain.vo.FriendVo;
import com.domain.vo.PageVo;
import com.mapper.FriendApplyMapper;
import com.mapper.FriendRelationMapper;
import com.mapper.UserMapper;
import com.service.IFriendService;
import com.utils.PageHelper;
import com.utils.Query;
import com.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendRelationMapper, FriendRelationPo> implements IFriendService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendApplyMapper applyMapper;
    @Autowired
    private FriendRelationMapper friendRelationMapper;

    @Override
    @Transactional
    public ResponseResult<Void> sendApply(FriendApplyDto dto, Long userId) {
        // 1. 校验目标用户是否存在
        UserPo targetUser = userMapper.selectById(dto.getTargetUserId());
        if (targetUser == null) {
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        // 2. 校验是否为自己
        if (userId.equals(dto.getTargetUserId())) {
            throw new EIException(BusinessCodeEnum.CANNOT_ADD_SELF);
        }
        // 3. 校验是否存在未处理申请
        FriendApplyPo pendingApply = applyMapper.selectPendingApply(userId, dto.getTargetUserId());
        if (pendingApply != null) {
            throw new EIException(BusinessCodeEnum.FRIEND_APPLY_EXIST);
        }
        // 4. 校验是否已是好友
        if (isMutualFriend(userId, dto.getTargetUserId())) {
            throw new EIException(BusinessCodeEnum.ALREADY_FRIENDS);
        }
        // 5. 保存申请记录
        FriendApplyPo applyPo = FriendConverter.INSTANCE.friendApplyDtoToFriendApplyPo(dto, userId);
        applyPo.setApplyTime(LocalDateTime.now());
        applyMapper.insert(applyPo);
        return ResponseResult.success(BusinessCodeEnum.FRIEND_APPLY_SEND_SUCCESS);
    }
    @Override
    public ResponseResult<PageVo<FriendApplyVo>> getApplyList(Long userId, PageQuery pageQuery) {
        PageQuery queryParam;
        if(pageQuery==null){
            queryParam=new PageQuery();
            queryParam.setSortBy("apply_time");
        }else {
            queryParam=pageQuery;
        }
        Query<FriendApplyPo> query = new Query<>(queryParam);
        Page<FriendApplyPo> page = query.getPage();

        IPage<FriendApplyPo> applyPage = applyMapper.selectMyFriendApplyPage(page, userId);
        List<FriendApplyPo> applyList = applyPage.getRecords();

        if (applyList.isEmpty()) {
            return ResponseResult.success(PageVo.of(Collections.emptyList(), 0L,
                    (int) page.getCurrent(), (int) page.getSize()
            ));
        }
        List<FriendApplyVo> friendApplyVoList=FriendConverter.INSTANCE.friendApplyPoListToFriendApplyVoList(applyList);
        //封装分页结果
        PageVo<FriendApplyVo> pageVo = PageVo.of(
                friendApplyVoList, applyPage.getTotal(),
                (int) page.getCurrent(), (int) page.getSize()
        );

        return ResponseResult.success(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult<Void> handleApply(FriendOperateDto dto, Long userId) {
        // 1. 查询申请记录并校验归属权
        FriendApplyPo apply = applyMapper.selectById(dto.getApplyId());
        if (apply == null || !userId.equals(apply.getTargetUserId())) {
            throw new EIException(BusinessCodeEnum.FRIEND_APPLY_NOT_EXIST);
        }
        // 2. 校验申请状态（仅处理待处理状态）
        if (apply.getStatus() != FriendStatusEnum.PENDING) {
            throw new EIException(BusinessCodeEnum.FRIEND_APPLY_ALREADY_HANDLED);
        }
        // 3. 更新申请状态
        apply.setStatus(dto.getStatus());
        apply.setUpdateTime(LocalDateTime.now());
        applyMapper.updateById(apply);

        // 4. 若同意，创建双向好友关系
        if (FriendStatusEnum.CONFIRMED.equals(dto.getStatus())) {
            FriendRelationPo relationA = FriendConverter.INSTANCE.applyToRelationA(apply);
            FriendRelationPo relationB = FriendConverter.INSTANCE.applyToRelationB(apply);
            save(relationA);
            save(relationB);
        }

        return ResponseResult.success();
    }

    @Override
    public ResponseResult<PageVo<FriendVo>> getFriendList(Long userId, PageQuery pageQuery) {
        PageQuery queryParam;
        if(pageQuery==null){
            queryParam=new PageQuery();
            queryParam.setSortBy("conform_time");
        }else {
            queryParam=pageQuery;
        }
        // 构建分页对象
        Query<FriendRelationPo> query = new Query<>(queryParam);
        Page<FriendRelationPo> page = query.getPage();

        // 构建查询条件（仅查询已确认的好友）
        LambdaQueryWrapper<FriendRelationPo> wrapper = new LambdaQueryWrapper<FriendRelationPo>()
                .eq(FriendRelationPo::getUserId, userId)
                .eq(FriendRelationPo::getStatus, FriendStatusEnum.CONFIRMED);

        // 执行分页查询
        IPage<FriendVo> friendPage = friendRelationMapper.selectFriendPage(page, wrapper);

        // 封装分页结果（直接使用FriendVo）
        PageVo<FriendVo> pageVo = PageVo.of(
                friendPage.getRecords(),friendPage.getTotal(),
                queryParam.getPage(), queryParam.getSize()
        );

        return ResponseResult.success(pageVo);
    }

    @Override
    public boolean isMutualFriend(Long userId, Long friendId) {
        Integer count = friendRelationMapper.checkMutualFriend(userId, friendId);
        return count != null && count > 0;
    }

    @Override
    @Transactional
    public ResponseResult<Void> deleteFriend(Long friendId, Long currentUserId) {
        // 1. 校验好友关系是否存在
        if (!isMutualFriend(currentUserId, friendId)) {
            throw new EIException(BusinessCodeEnum.NOT_FRIENDS);
        }
        // 2. 删除双向好友关系
        LambdaQueryWrapper<FriendRelationPo> relationWrapper = new LambdaQueryWrapper<FriendRelationPo>()
                .and(w -> w.eq(FriendRelationPo::getUserId, currentUserId)
                        .eq(FriendRelationPo::getFriendId, friendId))
                .or(w -> w.eq(FriendRelationPo::getUserId, friendId)
                        .eq(FriendRelationPo::getFriendId, currentUserId));
        friendRelationMapper.delete(relationWrapper);

        // 3. 清理相关申请记录
        LambdaQueryWrapper<FriendApplyPo> applyWrapper = new LambdaQueryWrapper<FriendApplyPo>()
                .and(w -> w.eq(FriendApplyPo::getApplyUserId, currentUserId)
                        .eq(FriendApplyPo::getTargetUserId, friendId))
                .or(w -> w.eq(FriendApplyPo::getApplyUserId, friendId)
                        .eq(FriendApplyPo::getTargetUserId, currentUserId));
        applyMapper.delete(applyWrapper);

        return ResponseResult.success(BusinessCodeEnum.FRIEND_DELETE_SUCCESS);
    }


}