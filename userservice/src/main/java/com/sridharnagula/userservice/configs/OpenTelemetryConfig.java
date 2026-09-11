package com.sridharnagula.userservice.configs;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes a {@link Tracer} bean backed by the OpenTelemetry instance that the
 * opentelemetry-javaagent installs as the GlobalOpenTelemetry singleton at JVM
 * startup (see Dockerfile / -javaagent flag). Application code can therefore
 * create additional child spans that are automatically correlated with the
 * spans the agent already creates for the inbound HTTP request, without the
 * service needing to configure its own SDK, processors or exporters.
 */
@Configuration
public class OpenTelemetryConfig {

    @Bean
    public Tracer tracer() {
        return GlobalOpenTelemetry.getTracer("com.sridharnagula.userservice", "0.0.1");
    }
}
