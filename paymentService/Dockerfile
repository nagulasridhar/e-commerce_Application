# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---- OpenTelemetry Java agent stage ----
FROM curlimages/curl:8.10.1 AS otel
RUN curl -sSfL -o /tmp/opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /build/target/paymentService-0.0.1-SNAPSHOT.jar app.jar
COPY --from=otel /tmp/opentelemetry-javaagent.jar otel-javaagent.jar

ENV OTEL_SERVICE_NAME=paymentservice \
    OTEL_TRACES_EXPORTER=otlp \
    OTEL_METRICS_EXPORTER=otlp \
    OTEL_LOGS_EXPORTER=otlp \
    OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4318 \
    OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
    OTEL_RESOURCE_ATTRIBUTES=service.namespace=ecommerce,deployment.environment=docker \
    OTEL_INSTRUMENTATION_LOGBACK_APPENDER_EXPERIMENTAL_LOG_ATTRIBUTES=true

EXPOSE 8083
ENTRYPOINT ["java", "-javaagent:/app/otel-javaagent.jar", "-jar", "/app/app.jar"]
