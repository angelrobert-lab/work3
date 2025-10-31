package com.converter;

import com.domain.dto.RegisterDto;
import com.domain.dto.UserDto;
import com.domain.dto.UserInformationDto;
import com.domain.po.UserPo;
import com.domain.vo.UserInformationVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户实体转换器
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * 从 RegisterDto 转换为 UserPo
     */
    @Mapping(source = "firstPassword", target = "password")
    @Mapping(target = "gender", expression = "java(0)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)

    UserPo registerDtoToUserPo(RegisterDto registerDTO);



    /**
     * 从 UserPo 转换为 UserDto
     */
    UserDto userPoToUserDto(UserPo userPo);
    // UserDto -> UserPo（用于更新用户信息时的DTO转PO）
    UserPo userDtoToUserPo(UserDto dto);
    @Mapping(target = "id", ignore = true) // ID不允许通过DTO修改
    @Mapping(target = "password", ignore = true) // 密码不允许通过此DTO修改
    @Mapping(target = "role", ignore = true) // 角色不允许通过此DTO修改
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserPo userInfoDtoToUserPo(UserInformationDto dto);

    /**
     * 将UserPo转换为UserInformationVo（用于返回用户信息）
     */
    UserInformationVo userPoToUserInfoVo(UserPo userPo);

    // 集合转换（批量查询用户时使用）
    List<UserDto> userPoListToUserDtoList(List<UserPo> poList);
}
