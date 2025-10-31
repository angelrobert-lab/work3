package com.utils;

import cn.hutool.core.util.RandomUtil;
import com.common.enums.BusinessCodeEnum;
import com.domain.po.EIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaUtil {

    @Autowired
    private RedisUtil redisUtil;
    // 验证码有效期5分钟
    private static final int CAPTCHA_EXPIRE = 300;

    /**
     * 生成验证码并存储到Redis
     */
    public String generateCaptcha(String phone) {
        String captcha = RandomUtil.randomNumbers(6);
        redisUtil.set("captcha:" + phone, captcha, CAPTCHA_EXPIRE);
        return captcha;
    }

    /**
     * 验证验证码是否正确
     */
    public void validateCaptcha(String phone, String inputCaptcha) {
        String key = "captcha:" + phone;
        String storedCaptcha = (String) redisUtil.get(key);

        if (storedCaptcha == null) {
            throw new EIException(BusinessCodeEnum.CAPTCHA_ERROR);
        }

        if (!storedCaptcha.equals(inputCaptcha)) {
            throw new EIException(BusinessCodeEnum.CAPTCHA_ERROR);
        }
        redisUtil.del(key);
    }
}