package top.kkuily.xingbackend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小K
 * @description MyBatis Plus 的配置类
 */
@Configuration
@MapperScan("top.kkuily.xingbackend.mapper")
public class MyBaitsPlusConfig {
}
