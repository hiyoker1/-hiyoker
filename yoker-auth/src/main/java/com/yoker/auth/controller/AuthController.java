package com.yoker.auth.controller;

import com.yoker.auth.dto.LoginDTO;
import com.yoker.auth.util.JwtUtil;
import com.yoker.auth.vo.TokenVO;
import com.yoker.common.result.Result;
import com.yoker.common.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<TokenVO> login(@RequestBody LoginDTO dto) {
        // 暂时硬编码，后续接 system 服务查数据库
        if (!"admin".equals(dto.getUsername()) || !"123456".equals(dto.getPassword())) {
            throw new GlobalException("用户名或密码错误");
        }

        // 生成 Token
        String token = jwtUtil.generateToken(dto.getUsername());

        // 存入 Redis，key: token:用户名
        redisTemplate.opsForValue().set(
                "token:" + dto.getUsername(),
                token,
                7200,
                TimeUnit.SECONDS
        );

        return Result.ok(TokenVO.build(token, dto.getUsername()));
    }

    /**
     * 登出
     */
    @DeleteMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsername(token.replace("Bearer ", ""));
        redisTemplate.delete("token:" + username);
        return Result.ok();
    }

    /**
     * 验证 Token
     */
    @GetMapping("/validate")
    public Result<String> validate(@RequestHeader("Authorization") String token) {
        String realToken = token.replace("Bearer ", "");
        if (jwtUtil.isExpired(realToken)) {
            throw new GlobalException("Token已过期");
        }
        return Result.ok(jwtUtil.getUsername(realToken));
    }
}