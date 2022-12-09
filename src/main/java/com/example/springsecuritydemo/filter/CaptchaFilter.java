package com.example.springsecuritydemo.filter;

import com.example.springsecuritydemo.constants.Const;
import com.example.springsecuritydemo.exception.CaptchaException;
import com.example.springsecuritydemo.handle.LoginFailureHandler;
import com.example.springsecuritydemo.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ycy
 * 验证码过滤器/验证码拦截器
 * <p>
 * 请求发向servlet时会被Filter拦截，如果servlet将请求转发给另一个servlet，
 * 请求发向第二个servlet时，依旧会被相同的Filter拦截。
 * 结果就是一个请求被同一个Filter拦截了两次
 * <p>
 * OncePerRequestFilter：一个请求只被过滤器拦截一次。请求转发不会第二次触发过滤器。
 * 它能够确保在一次请求中只通过一次filter，而不会重复执行
 */
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("开始使用redis进行验证码校验......");
        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            //若 请求是登录请求 则进行验证码校验
            try {
                validate(request);
            } catch (CaptchaException e) {
                //验证码异常交给 登录失败处理器 处理
                //否则进入 JwtAuthenticationFilter 进行 jwt验证
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        //???这里不懂
        filterChain.doFilter(request, response);
    }

    //验证码校验
    private void validate(HttpServletRequest request) {
        String key = request.getParameter("key");
        String code = request.getParameter("code");
        //验证码校验失败则抛出验证码异常
        //该异常由 登录失败处理器LoginFailureHandler 处理
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码为空");
        }
        Object hget = redisUtils.hget(Const.CAPTCHA_KEY, key);
        log.info("redis中获取的验证码为:" + hget);
        if (!code.equals(hget)) {
            throw new CaptchaException("验证码错误");
        }
        if (hget == null || hget.equals("")) {
            throw new CaptchaException("刷新验证码");
        }
        //验证码校验成功则将redis中的验证码删除
        redisUtils.hdel(Const.CAPTCHA_KEY, key);
    }
}
