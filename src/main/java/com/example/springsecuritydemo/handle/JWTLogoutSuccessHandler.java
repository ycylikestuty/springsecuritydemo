package com.example.springsecuritydemo.handle;

import cn.hutool.json.JSONUtil;
import com.example.springsecuritydemo.entity.Result;
import com.example.springsecuritydemo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ycy
 * 登出处理器
 */
@Component
@Slf4j
public class JWTLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //将之前置入SecurityContext中的用户信息进行清除
        //(通过创建SecurityContextLogoutHandler对象，调用它的logout方法完成
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        response.setContentType("application/json;charset=UTF-8");

        //采取 置空策略 来清除浏览器中保存的JWT
        //将原来的JWT置为空返给前端，前端会将空字符串覆盖之前的jwt
        // 因为JWT是无状态化的，销毁JWT是做不到的，JWT生成之后，只有等JWT过期之后，才会失效。
        response.setHeader(jwtUtils.getHeader(), "");
        ServletOutputStream outputStream = response.getOutputStream();

        Result result = Result.success("SuccessLogout");
        log.info("成功登出");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
