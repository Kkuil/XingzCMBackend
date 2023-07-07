package top.kkuily.xingbackend;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class testJasypt {
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void test01() {
        String username = stringEncryptor.encrypt("root");
        String password = stringEncryptor.encrypt("wwq5714806");
        log.info("username 密文: " + username);
        log.info("password 密文: " + password);
    }
}
