package com.example.springsecuritydemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsecuritydemo.entity.SysMenu;
import com.example.springsecuritydemo.mapper.SysMenuMapper;
import com.example.springsecuritydemo.service.SysMenuService;
import org.springframework.stereotype.Service;

/**
 * @author ycy
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
}
