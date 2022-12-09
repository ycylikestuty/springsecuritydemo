package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecuritydemo.entity.SysUserRole;
import com.example.springsecuritydemo.mapper.SysUserRoleMapper;
import com.example.springsecuritydemo.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
