package top.kkuily.xingbackend.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

import static top.kkuily.xingbackend.constant.admin.Login.*;

/**
 * @author 小K
 * @description token 工具包
 */
@Slf4j
public class Token {


    /**
     * 生成token
     *
     * @param data HashMap<String, Object>
     * @return String
     */
    public static String create(HashMap<String, Object> data, String secret) {
        try {
            return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .setClaims(data)
                    .setExpiration(new Date(System.currentTimeMillis() + ADMIN_TOKEN_TTL))
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析token
     *
     * @param token String
     * @return String
     */
    public static Claims parse(String token, String secret) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
