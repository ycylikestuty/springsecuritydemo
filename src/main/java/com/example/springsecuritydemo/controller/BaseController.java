package com.example.springsecuritydemo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springsecuritydemo.service.*;
import com.example.springsecuritydemo.util.RedisUtils;
import org.springframework.web.bind.ServletRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ycy
 */
public class BaseController<T> {
    @Resource
    HttpServletRequest request;

    @Resource
    RedisUtils redisUtils;

    @Resource
    SysUserService sysUserService;

    @Resource
    SysMenuService sysMenuService;

    @Resource
    SysRoleService sysRoleService;

    @Resource
    SysRoleMenuService sysRoleMenuService;

    @Resource
    SysUserRoleService sysUserRoleService;

    public Page<T> getPage(){
        int pageNum = ServletRequestUtils.getIntParameter(request, "pageNum", 1);
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
        return new Page<T>(pageNum, pageSize);
    }
}
