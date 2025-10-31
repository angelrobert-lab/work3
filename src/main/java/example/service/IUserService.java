package com.service;

import com.domain.dto.*;
import com.domain.po.UserPo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.vo.UserInformationVo;


public interface IUserService extends IService<UserPo> {

    UserDto getByUsername(String username);

    String login(LoginDto loginDTO);

    void register(RegisterDto registerDTO);

    UserInformationVo getPeraonlInfo(Long userId);

    UserInformationVo updateUserInfo(UserInformationDto infoDto, TokenDto tokenDto);
    String sendResetCaptcha(String phone);
    void resetPassword(ResetPasswordDto dto);
}
