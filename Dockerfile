FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src src
RUN ./gradlew --no-daemon bootJar

FROM eclipse-temurin:21-jre
ENV TZ=Asia/Seoul
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=60"
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app
COPY --from=build /app/build/libs/*.jar app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
