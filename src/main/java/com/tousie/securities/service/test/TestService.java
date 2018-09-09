package com.tousie.securities.service.test;

import com.sonluo.spongebob.spring.server.ApiService;
import com.sonluo.spongebob.spring.server.ApiServiceMapping;

@ApiService
@ApiServiceMapping("test")
public class TestService {

    @ApiServiceMapping("test")
    public ResponseTest test(RequestTest requestTest) {
        ResponseTest responseTest = new ResponseTest();
        responseTest.setBar(requestTest.getBar());
        responseTest.setFoo(requestTest.getFoo());
        return responseTest;
    }

    public static class RequestTest {
        private String foo;
        private String bar;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    public static class ResponseTest {
        private String foo;
        private String bar;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }
    }
}
