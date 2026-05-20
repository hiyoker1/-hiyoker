package com.yoker.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoker.common.result.PageResult;
import com.yoker.common.result.Result;
import com.yoker.system.entity.SysUser;
import com.yoker.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @GetMapping("/page")
    public Result<PageResult<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {

        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreateTime);

        userService.page(page, wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody SysUser user) {
        userService.save(user);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody SysUser user) {
        userService.updateById(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.ok();
    }
}