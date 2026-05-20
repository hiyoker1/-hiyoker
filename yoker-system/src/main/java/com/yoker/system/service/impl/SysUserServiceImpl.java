package com.yoker.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yoker.system.entity.SysUser;
import com.yoker.system.mapper.SysUserMapper;
import com.yoker.system.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {
}