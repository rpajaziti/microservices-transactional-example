package com.example.common.config;

import info.developerblog.spring.thrift.client.ThriftClientHeaderCustomizer;
import io.seata.core.context.RootContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Map;

@Configuration
@ConditionalOnClass(name = "info.developerblog.spring.thrift.client.ThriftClientHeaderCustomizer")
public class ThriftConfig {

    @Bean
    public ThriftClientHeaderCustomizer seataHeaderCustomizer() {
        return () -> {
            String xid = RootContext.getXID();
            return StringUtils.hasText(xid) ? Map.of(RootContext.KEY_XID, xid) : Map.of();
        };
    }
}
