package com.yoker.auth.vo;

import lombok.Data;

@Data
public class TokenVO {

    /** token */
    private String token;

    /** 用户名 */
    private String username;

    public static TokenVO build(String token, String username) {
        TokenVO vo = new TokenVO();
        vo.setToken(token);
        vo.setUsername(username);
        return vo;
    }
}