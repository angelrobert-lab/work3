package com.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.annotation.APPLoginUser;
import com.annotation.IgnoreAuth;
import com.common.enums.BusinessCodeEnum;
import com.converter.UserConverter;
import com.domain.dto.*;
import com.domain.vo.UserInformationVo;
import com.service.impl.UserServiceImpl;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Schema(description = "用户相关接口")
@RequestMapping("users")
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userService;

	@Operation(summary = "用户登录")
	@PostMapping("/login")
	public ResponseResult<String> login(@Valid @RequestBody LoginDto loginDto) {
		return ResponseResult.success(BusinessCodeEnum.LOGIN_SUCCESS, userService.login(loginDto));
	}

	@Operation(summary = "用户退出")
	@PostMapping("/logout")
	public ResponseResult<Void> logout() {
		StpUtil.logout();
		return ResponseResult.success();
	}
	@IgnoreAuth
	@Operation(summary = "用户注册")
	@PostMapping("/register")
	public ResponseResult<Void> register(@Valid @RequestBody RegisterDto registerDTO) {
		userService.register(registerDTO);
		userService.save(UserConverter.INSTANCE.registerDtoToUserPo(registerDTO));
		return ResponseResult.success(BusinessCodeEnum.ReGISTER_SUCCESS);
	}

	@Operation(summary = "获取个人信息")
	@GetMapping("/info")
	@SaCheckPermission("user.query")
	public ResponseResult<UserInformationVo> getCurrentUserInfo() {
		UserInformationVo userInformationVo = userService.getPeraonlInfo(StpUtil.getLoginIdAsLong());
		return ResponseResult.success(userInformationVo);
	}

	@Operation(summary = "修改当前用户个人信息")
	@PutMapping("/info")
	@SaCheckPermission("user.update")
	public ResponseResult<UserInformationVo> updateUserInfo(
			@Valid @RequestBody UserInformationDto infoDto,
			@APPLoginUser TokenDto tokenDto
	) {
		UserInformationVo userInformationVo = userService.updateUserInfo(infoDto, tokenDto);
		return ResponseResult.success(userInformationVo);
	}

	@Operation(summary = "刷新token")
	@PostMapping("/refreshToken")
	public ResponseResult<String> refreshToken() {
		long newTimeout = System.currentTimeMillis() + 30 * 60 * 1000;
		StpUtil.renewTimeout(newTimeout);
		return ResponseResult.success(StpUtil.getTokenValue());
	}

	@Operation(summary = "删除用户（仅管理员）")
	@DeleteMapping("/{userId}")
	@SaCheckRole("admin")
	@SaCheckPermission("user.delete")
	public ResponseResult<Void> deleteUser(@PathVariable Long userId) {
		userService.removeById(userId);
		return ResponseResult.success();
	}

	@Operation(summary = "获取所有用户列表")
	@GetMapping("/list")
	@SaCheckRole("admin")
	@SaCheckPermission("user.list")
	public ResponseResult<Object> getUserList() {
		return ResponseResult.success(userService.list());
	}

	@Operation(summary = "发送重置密码验证码")
	@PostMapping("/reset/sendCaptcha")
	public ResponseResult<String> sendResetCaptcha(@RequestParam String phone) {
		return ResponseResult.success(userService.sendResetCaptcha(phone));
	}

	@Operation(summary = "重置密码")
	@PostMapping("/reset/password")
	public ResponseResult<Void> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
		userService.resetPassword(dto);
		return ResponseResult.success(BusinessCodeEnum.SUCCESS);
	}
}
