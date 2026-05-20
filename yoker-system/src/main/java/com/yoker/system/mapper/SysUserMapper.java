package com.yoker.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yoker.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}