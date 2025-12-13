FROM bellsoft/liberica-runtime-container:jre-21-slim-glibc AS optimizer
WORKDIR /optimizer
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-runtime-container:jre-21-slim-glibc  AS runtime
WORKDIR /application
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser
EXPOSE 8080
COPY --from=optimizer /optimizer/extracted/dependencies/ ./
COPY --from=optimizer /optimizer/extracted/spring-boot-loader/ ./
COPY --from=optimizer /optimizer/extracted/snapshot-dependencies/ ./
COPY --from=optimizer /optimizer/extracted/application/ ./
ENTRYPOINT ["java", "-jar", "application.jar"]
