package com.controller;

import com.annotation.APPLoginUser;
import com.common.enums.BusinessCodeEnum;
import com.domain.dto.FriendApplyDto;
import com.domain.dto.FriendDto;
import com.domain.dto.FriendOperateDto;
import com.domain.dto.TokenDto;
import com.domain.query.PageQuery;
import com.domain.vo.FriendApplyVo;
import com.domain.vo.FriendVo;
import com.domain.vo.PageVo;
import com.service.IFriendService;
import com.service.impl.FriendServiceImpl;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@Tag(name = "好友管理", description = "好友相关接口")
public class FriendController {

    @Autowired
    private FriendServiceImpl friendService;

    @Operation(summary = "发送好友申请")
    @PostMapping("/apply")
    public ResponseResult<Void> sendApply(
            @Valid @RequestBody FriendApplyDto dto,
            @APPLoginUser TokenDto tokenDto
    ) {
        return friendService.sendApply(dto, tokenDto.getUserId());
    }

    @Operation(summary = "获取好友申请列表")
    @GetMapping("/apply")
    public ResponseResult<PageVo<FriendApplyVo>> getApplyList(
            @APPLoginUser TokenDto tokenDto,
            @Valid PageQuery pageQuery
    ) {
        return friendService.getApplyList(tokenDto.getUserId(), pageQuery);
    }
    @Operation(summary = "处理好友申请")
    @PostMapping("/handle")
    public ResponseResult<Void> handleApply(
            @Valid @RequestBody FriendOperateDto dto,
            @APPLoginUser TokenDto tokenDto
    ) {
        return friendService.handleApply(dto, tokenDto.getUserId());
    }

    @Operation(summary = "获取好友列表")
    @GetMapping("/list")
    public ResponseResult<PageVo<FriendVo>> getFriendList(
            @APPLoginUser TokenDto tokenDto,
            @Valid PageQuery pageQuery
    ) {
        return friendService.getFriendList(tokenDto.getUserId(),pageQuery);
    }

    @Operation(summary = "删除好友")
    @DeleteMapping("/{friendId}")
    public ResponseResult<Void> deleteFriend(
            @PathVariable Long friendId,
            @APPLoginUser TokenDto tokenDto
    ) {
        return friendService.deleteFriend(friendId, tokenDto.getUserId());
    }
}