package com.example.springsecuritydemo.handle;

import cn.hutool.json.JSONUtil;
import com.example.springsecuritydemo.entity.Result;
import com.example.springsecuritydemo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author ycy
 * 登录认证成功处理器
 */
@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("开始进行登录处理......");
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        //生成jwt，并将jwt置入响应头
        //在JwtAuthenticationFilter中取出jwt
        String jwt = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), jwt);
        log.info("生成的jwt为:"+jwt);
        Result success = Result.success("登录成功");
        outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * AuthenticationSuccessHandler有两个需要重写的方法
     * 另一个比该方法多了个FilterChain chain参数
     */
}
