package com.sonluo.spongebob.spring.server;

import java.lang.annotation.*;

/**
 * @author sunqian
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiServiceMapping {

    String value() default "";

    String group() default "";

    String[] include() default {};

    String[] exclude() default {};
}
