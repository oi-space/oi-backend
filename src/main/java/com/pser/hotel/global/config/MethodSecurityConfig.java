package com.pser.hotel.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,  // Spring Seucirty의 @PreAuthowirze, @PreFilter / @PostAuthorize, @PostFilter 어노테이션 활성화 여부
        securedEnabled = true,  // @Secured 어노테이션 활성화 여부
        jsr250Enabled = true    // @RolleAllowed 어노테이션 사용 활성화 여부
)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
