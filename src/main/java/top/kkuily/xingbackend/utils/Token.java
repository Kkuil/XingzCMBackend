package top.kkuily.xingbackend.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

import static top.kkuily.xingbackend.constant.Login.*;

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
    public static String create(HashMap<String, Object> data) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "xingz_cm_123456")
                .setClaims(data)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL))
                .compact();
    }

    /**
     * 解析token
     *
     * @param token String
     * @return String
     */
    public static Claims parse(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException |
                 UnsupportedJwtException e) {
            e.printStackTrace();
            return null;
        }
    }
}
