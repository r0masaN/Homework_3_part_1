package com.weyland.synthetichumancorestarter.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.weyland.synthetichumancorestarter.audit")
@EnableAspectJAutoProxy
public class WeylandAutoConfiguration {

}
