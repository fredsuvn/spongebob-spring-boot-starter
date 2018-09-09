package com.tousie.securities.port.http;

import com.sonluo.spongebob.spring.server.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class HttpPort {

    @Resource
    private Server globalServer;

    @RequestMapping("service/{url:.+}")
    public Object port(@PathVariable("url") String url, @RequestParam Map<String, Object> requestParam) {
        Object result = globalServer.doService(new Request() {
            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public String getRemoteAddress() {
                return "";
            }

            @Override
            public Object getContent() {
                return requestParam;
            }

            @Override
            public Client getClient() {
                return null;
            }

            @Override
            public Session getSession(boolean create) {
                return null;
            }
        });
        return result;
    }
}
