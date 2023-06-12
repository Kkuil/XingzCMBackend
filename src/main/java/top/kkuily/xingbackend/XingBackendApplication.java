package top.kkuily.xingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.kkuily.xingbackend.service.other.SmsCaptchaService;

/**
 * @author 小K
 */
@SpringBootApplication
@EnableConfigurationProperties({SmsCaptchaService.class})
public class XingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(XingBackendApplication.class, args);
	}

}
