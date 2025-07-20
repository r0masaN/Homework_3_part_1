package com.weyland.synthetichumancorestarter.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringJoiner;

@Aspect @Component
public class WeylandAuditAspect {
    private static final Logger LOG = LoggerFactory.getLogger(WeylandAuditAspect.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public WeylandAuditAspect(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Around("@annotation(WeylandWatchingYou)")
    public Object auditMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final WeylandWatchingYou annotation = method.getAnnotation(WeylandWatchingYou.class);
        final OutputType outputType = annotation.output();
        final String methodName = method.getName();
        final Object[] args = joinPoint.getArgs();

        Object result = null;
        Throwable error = null;
        try {
            result = joinPoint.proceed(args);
            return result;

        } catch (Throwable e) {
            error = e;
            throw e;

        } finally {
            final StringJoiner logJoiner = new StringJoiner("\r\n\t");
            logJoiner.add("[AUDIT]").add(String.format("Method: %s", methodName))
                    .add(String.format("Args: %s", Arrays.toString(args)));

            if (error == null) {
                logJoiner.add(String.format("Result: %s", result));
            } else {
                logJoiner.add(String.format("Error: %s", error.getMessage()));
            }

            switch (outputType) {
                case CONSOLE -> LOG.info(logJoiner.toString());
                case KAFKA -> this.kafkaTemplate.send("weyland-audit", logJoiner.toString());
            }
        }
    }
}
