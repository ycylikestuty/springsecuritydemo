package com.example.springsecuritydemo.controller;

import cn.hutool.core.map.MapUtil;
import com.example.springsecuritydemo.constants.Const;
import com.example.springsecuritydemo.entity.Result;
import com.example.springsecuritydemo.util.RedisUtils;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ycy
 */
@RestController
@Slf4j
public class KaptchaController {
    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        log.info("开始生成验证码......");
        //生成随机码，代表某一个用户
        String key = UUID.randomUUID().toString();
        //生成字符串验证码
        String code = producer.createText();
        //生成验证码图片
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        //使用base64对图片进行编码
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String img = str + encoder.encode(outputStream.toByteArray());
        //设置过期时间为60分钟
        redisUtils.hset(Const.CAPTCHA_KEY, key, code, 60 * 60);
        //打印key和code
        log.info("生成的key：" + key);
        log.info("生成的code：" + code);
        //返回随机码和验证码图片给前端
        return Result.success(
                MapUtil.builder()
                        //map的key必须要与前端对应
                        .put("captcherImg", img)
                        .put("userKey", key)
                        .build()
        );
    }
}
