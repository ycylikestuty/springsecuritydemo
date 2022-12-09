package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecuritydemo.entity.SysRole;
import com.example.springsecuritydemo.mapper.SysRoleMapper;
import com.example.springsecuritydemo.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
