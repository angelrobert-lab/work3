
package com.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.po.ConfigPo;
import com.domain.vo.PageVo;

import java.util.Map;


/**
 * 系统用户
 */
public interface IConfigService extends IService<ConfigPo> {
	PageVo<ConfigPo> queryPage(Map<String, Object> params);
}
