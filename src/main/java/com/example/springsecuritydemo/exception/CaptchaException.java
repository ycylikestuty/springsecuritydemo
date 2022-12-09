package com.example.springsecuritydemo.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author ycy
 * 验证码错误异常
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg){
        super(msg);
    }
}
