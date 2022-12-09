package com.example.springsecuritydemo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springsecuritydemo.entity.SysUser;

/**
 * @author ycy
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getByUserName(String username);

    /**
     * 根据id获取用户的角色和菜单权限操作
     *
     * @param userId id
     * @return 角色和菜单权限操作信息
     */
    String getUserAuthorityInfo(Long userId);
}
