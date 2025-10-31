package com.domain.po;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* 类说明 : 
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("config")
public class ConfigPo implements Serializable{
private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * key
	 */
	private String name;
	
	/**
	 * value
	 */
	private String value;
	
}
