package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecuritydemo.entity.SysRoleMenu;
import com.example.springsecuritydemo.mapper.SysRoleMenuMapper;
import com.example.springsecuritydemo.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
}
