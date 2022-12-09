package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.entity.AccountUser;
import com.example.springsecuritydemo.entity.SysUser;
import com.example.springsecuritydemo.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ycy
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始用户登录,用户名为{}",username);
        SysUser sysUser=sysUserService.getByUserName(username);
        if(sysUser==null){
            throw new UsernameNotFoundException("用户名或密码错误2");
        }
        //返回UserDetail对象
        //UserDetail对象包括 用户信息（账号/用户名、密码） 和 权限信息（角色、操作目录）
        //Spring Security在拿到UserDetails之后，会去对比Authentication
        // （默认使用的是UsernamePasswordAuthenticationFilter,它会读取表单中的用户信息并生成Authentication），
        // 若密码正确，则Spring Secuity自动帮忙完成登录
        // 在该程序中是进入生成jwt阶段即LoginSuccessHandler处理器
        return new AccountUser(sysUser.getId(),sysUser.getUsername(),sysUser.getPassword(),null);
    }

    //获取用户的角色（ROLE_admin）/菜单操作权限（sys:user:list）
    // 具体根据数据库情况
    public List<GrantedAuthority> getUserAuthority(Long userId){
        String authority=sysUserService.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
