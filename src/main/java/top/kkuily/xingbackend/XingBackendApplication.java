package top.kkuily.xingbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.kkuily.xingbackend.utils.SmsCaptcha;

/**
 * @author 小K
 */
@SpringBootApplication
@EnableConfigurationProperties({SmsCaptcha.class})
public class XingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(XingBackendApplication.class, args);
	}

}
