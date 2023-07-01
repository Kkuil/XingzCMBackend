package top.kkuily.xingbackend.web;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class Example {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) {
        logger.info("Example xingz.default.log from {}", Example.class.getSimpleName());
        logger.error("Exa mple xingz.default.log from {}", Example.class.getSimpleName());
    }
}
