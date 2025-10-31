package com.converter;

import com.domain.dto.FriendApplyDto;
import com.domain.po.FriendApplyPo;
import com.domain.po.FriendRelationPo;
import com.domain.vo.FriendApplyVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 好友申请对象转换
 */
@Mapper(
        componentModel = "spring",
        imports = {LocalDateTime.class}  // 添加这里
)
public interface FriendConverter {

    FriendConverter INSTANCE = Mappers.getMapper(FriendConverter.class);

    /**
     * 转换逻辑：FriendApplyDto -> FriendApplyPo
     */
    @Mapping(target = "applyUserId", source = "currentUserId")
    @Mapping(target = "updateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(FriendStatusEnum.PENDING)")
    @Mapping(target = "applyTime", expression = "java(LocalDateTime.now())")
    FriendApplyPo friendApplyDtoToFriendApplyPo(FriendApplyDto dto, Long currentUserId);

    FriendApplyVo friendApplyPoToFriendApplyVo(FriendApplyPo po);

    List<FriendApplyVo> friendApplyPoListToFriendApplyVoList(List<FriendApplyPo> poList);

    @Mapping(target = "userId", source = "applyUserId")
    @Mapping(target = "friendId", source = "targetUserId")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "confirmTime", expression = "java(LocalDateTime.now())")
    FriendRelationPo applyToRelationA(FriendApplyPo apply);

    /**
     * 将申请记录转换为B->A的关系记录
     */
    @Mapping(target = "userId", source = "targetUserId")
    @Mapping(target = "friendId", source = "applyUserId")
    @Mapping(target = "status", constant = "CONFIRMED")
    @Mapping(target = "confirmTime", expression = "java(LocalDateTime.now())")
    FriendRelationPo applyToRelationB(FriendApplyPo apply);
}