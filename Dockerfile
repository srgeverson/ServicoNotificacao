FROM openjdk:17
WORKDIR /app

ARG JAR_FILE

COPY target/${JAR_FILE} /app/servico.jar
COPY wait-for-it.sh /app/wait-for-it.sh

RUN chmod +x /app/wait-for-it.sh && ls -la

CMD ["java", "-jar", "servico.jar"]