package com.sonluo.spongebob.spring.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author sunqian
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ApiService {
}
