package com.controller;

import com.annotation.IgnoreAuth;
import com.domain.po.ConfigPo;
import com.domain.vo.PageVo;
import com.service.impl.ConfigServiceImpl;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统配置控制器
 */
@RequestMapping("config")
@RestController
public class ConfigController {

    @Autowired
    private ConfigServiceImpl configService;


    @Operation(summary = "分页查询配置列表")
    @RequestMapping("/page")
    public ResponseResult<PageVo<ConfigPo>> page(@RequestParam Map<String, Object> params) {
        PageVo<ConfigPo> pageVo = configService.queryPage(params);
        return ResponseResult.success(pageVo);
    }


    @IgnoreAuth
    @Operation(summary = "忽略认证的分页查询配置列表")
    @RequestMapping("/list")
    public ResponseResult<PageVo<ConfigPo>> list(@RequestParam Map<String, Object> params) {
        PageVo<ConfigPo> pageVo = configService.queryPage(params);
        return ResponseResult.success(pageVo);
    }
    @Operation(summary = "查询配置详情")
    @RequestMapping("/info/{id}")
    public ResponseResult<ConfigPo> info(@PathVariable("id") Long id) {
        ConfigPo config = configService.getById(id);
        return ResponseResult.success(config);
    }
    @Operation(summary = "忽略认证的配置详情查询")
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public ResponseResult<ConfigPo> detail(@PathVariable("id") Long id) {
        ConfigPo config = configService.getById(id);
        return ResponseResult.success(config);
    }
    @Operation(summary="根据名称获取配置信息")
    @RequestMapping("/info")
    public ResponseResult<ConfigPo> infoByNameName(@RequestParam String name) {
        ConfigPo config = configService.lambdaQuery()
                .eq(ConfigPo::getName, name)
                .one();
        return ResponseResult.success(config);
    }
    @Operation(summary = "保存配置")
    @PostMapping("/save")
    public ResponseResult<Void> save(@RequestBody ConfigPo config) {
        configService.save(config);
        return ResponseResult.success();
    }
    @Operation(summary = "修改配置")
    @RequestMapping("/update")
    public ResponseResult<Void> update(@RequestBody ConfigPo config) {
        configService.updateById(config);
        return ResponseResult.success();
    }

    @Operation(summary = "删除配置")
    @RequestMapping("/delete")
    public ResponseResult<Void> delete(@RequestBody Long[] ids) {
        configService.removeByIds(Arrays.asList(ids));
        return ResponseResult.success();
    }
}