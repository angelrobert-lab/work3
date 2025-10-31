package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.po.ConfigPo;
import com.domain.vo.PageVo;
import com.mapper.ConfigMapper;
import com.service.IConfigService;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 系统配置服务实现类
 */
@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigPo> implements IConfigService {

	@Override
	public PageVo<ConfigPo> queryPage(Map<String, Object> params) {
		Page<ConfigPo> page = new Query<ConfigPo>(params).getPage();
		QueryWrapper<ConfigPo> queryWrapper = new QueryWrapper<>();
		Page<ConfigPo> resultPage = this.page(page, queryWrapper);

		return PageVo.of(resultPage.getRecords(), resultPage.getTotal(),
				(int) resultPage.getCurrent(), (int) resultPage.getSize());
	}
}