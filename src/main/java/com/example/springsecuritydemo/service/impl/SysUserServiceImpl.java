package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecuritydemo.entity.*;
import com.example.springsecuritydemo.mapper.SysUserMapper;
import com.example.springsecuritydemo.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ycy
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUser getByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, "1")
                .eq(SysUser::getUsername, username);
        SysUser sysUser = this.getOne(queryWrapper);
        return sysUser;
    }

    /**
     * 根据id获取用户的角色和菜单权限操作
     *
     * @param userId id
     * @return 角色和菜单权限操作信息
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {
        String authority = "";
        //获取用户角色
        SysUser user = this.getById(userId);
        SysUserRole sysUserRole = sysUserRoleService.getOne(
                new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId));
        SysRole sysRole = sysRoleService.getOne(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getId, sysUserRole.getRoleId()));
        authority = "ROLE_" + sysRole.getCode() + ",";

        //获取用户菜单操作权限
        //一个用户会具有很多的操作权限，所以查询结果是list，不是一条
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, sysRole.getId())
        );
        List<Long> menuId=sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        List<SysMenu> sysMenuList=sysMenuService.listByIds(menuId);
        String menuPerms = sysMenuList.stream().map(item -> "" + item.getPerms())
                .collect(Collectors.joining(","));
        authority = authority.concat(menuPerms);
        return authority;
    }
}
