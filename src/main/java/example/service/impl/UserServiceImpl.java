package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.common.enums.BusinessCodeEnum;
import com.converter.UserConverter;
import com.domain.dto.*;
import com.domain.po.EIException;
import com.domain.po.UserPo;
import com.domain.vo.UserInformationVo;
import com.mapper.UserMapper;
import com.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.CaptchaUtil;
import com.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-10-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements IUserService {
    @Autowired
    private CaptchaUtil captchaUtil;
    @Override
    public UserDto getByUsername(String username) {
        UserPo userPo =lambdaQuery().eq(UserPo::getUsername, username).one();
        if (userPo == null) {
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        return UserConverter.INSTANCE.userPoToUserDto(userPo);
    }
    private Boolean checkByPhone(String phone) {
        return lambdaQuery().eq(UserPo::getPhone, phone).one() != null;
    }

    @Override
    public String login(LoginDto loginDTO) {
        UserDto userDto = getByUsername(loginDTO.getUsername());
        if (!HashUtil.matchesPassword(loginDTO.getPassword(), userDto.getPassword())) {
            throw new EIException(BusinessCodeEnum.INVALID_CREDENTIALS);
        }
        StpUtil.login(userDto.getId());
        // 存储用户信息到Session
        StpUtil.getSession()
                .set("userId", userDto.getId())
                .set("userName", userDto.getUsername())
                .set("role", userDto.getRole());
        return StpUtil.getTokenValue();
    }
    @Override
    public void register(RegisterDto registerDTO) {
        UserDto userDto = getByUsername(registerDTO.getUsername());
        if (userDto != null) {
            throw new EIException(BusinessCodeEnum.DUPLICATE_USERNAME);
        }
        if(checkByPhone(registerDTO.getPhone())){
            throw new EIException(BusinessCodeEnum.Phone_Is_Registered);
        }
    }

    @Override
    public UserInformationVo getPeraonlInfo(Long userId) {
        UserPo userPo =getById(userId);
        if(userPo == null){
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        return UserConverter.INSTANCE.userPoToUserInfoVo(userPo);
    }

    @Override
    public UserInformationVo updateUserInfo(UserInformationDto infoDto, TokenDto tokenDto) {
        UserPo userPo =getById(tokenDto.getUserId());
        if(userPo == null){
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        LambdaUpdateWrapper<UserPo> updateWrapper = new LambdaUpdateWrapper<UserPo>()
                .eq(UserPo::getId, tokenDto.getUserId())
                .set(infoDto.getUsername() != null, UserPo::getUsername, infoDto.getUsername())
                .set(infoDto.getPhone() != null, UserPo::getPhone, infoDto.getPhone())
                .set(infoDto.getGender() != null, UserPo::getGender, infoDto.getGender())
                .set(infoDto.getSignature() != null, UserPo::getSignature, infoDto.getSignature())
                .set(infoDto.getAvatar() != null, UserPo::getAvatar, infoDto.getAvatar())
                .set(UserPo::getUpdatedAt, LocalDateTime.now());
        update(updateWrapper);
        return UserConverter.INSTANCE.userPoToUserInfoVo(getById(tokenDto.getUserId()));
    }

    @Override
    public String sendResetCaptcha(String phone) {
        // 检查手机号是否已注册
        if (!checkByPhone(phone)) {
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
        // 生成并发送验证码
        return captchaUtil.generateCaptcha(phone);
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) {
        // 验证两次输入的密码是否一致
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new EIException(BusinessCodeEnum.PARAM_VALIDATION_ERROR);
        }
        // 验证验证码
        captchaUtil.validateCaptcha(dto.getPhone(), dto.getCaptcha());
        // 更新密码
        LambdaUpdateWrapper<UserPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserPo::getPhone, dto.getPhone())
                .set(UserPo::getPassword, HashUtil.encryptPassword(dto.getNewPassword()));

        boolean updateSuccess = update(updateWrapper);
        if (!updateSuccess) {
            throw new EIException(BusinessCodeEnum.USER_NOT_EXIST);
        }
    }
}
