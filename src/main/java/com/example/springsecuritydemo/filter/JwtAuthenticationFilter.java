package com.example.springsecuritydemo.filter;

import cn.hutool.core.util.StrUtil;
import com.example.springsecuritydemo.entity.SysUser;
import com.example.springsecuritydemo.service.SysUserService;
import com.example.springsecuritydemo.service.impl.UserDetailServiceImpl;
import com.example.springsecuritydemo.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ycy
 * jwt拦截器
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserDetailServiceImpl userDetailService;

    //构造函数
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JWT过滤器通过校验请求头token进行自动登录...");

        String jwt = request.getHeader(jwtUtils.getHeader());
        log.info("请求携带的jwt为：" + jwt);

        //若请求不包含jwt，对该请求不进行拦截，而是继续往后走
        //不包含jwt相当于匿名访问，一些接口可以匿名访问
        //若是一些接口不能匿名访问，则进入AccessDeniedHandler（即JwtAccessDeniedHandler）进行处理
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        //请求包含jwt
        Claims claims = jwtUtils.getClaimsByToken(jwt);
        log.info("请求的token：" + claims);
        //请求的token：{sub=admin, iat=1670482181, exp=1670485781} 即payload部分
        //iat 请求签发时间，exp 请求过期时间
        if (claims == null) {
            throw new JwtException("token异常");
        }
        if (jwtUtils.isTokenExpired(claims)) {
            throw new JwtException("token过期");
        }

        //jwt验证成功，获取jwt中的用户信息
        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUserName(username);

        //这里密码为null
        //因为提供了正确的jwt，所以实现了自动登录
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null,
                userDetailService.getUserAuthority(sysUser.getId()));
        log.info("该用户的权限为：" + userDetailService.getUserAuthority(sysUser.getId()));
        //将token交给SecurityContextHolder，set进它的context中，
        //后续就能通过调用SecurityContextHolder.getContext().getAuthentication().getPrincipal()等方法获取到当前登录的用户信息
        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);
    }
}
