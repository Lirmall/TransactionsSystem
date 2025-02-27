# Используем нужный образ java
FROM bellsoft/liberica-openjdk-alpine:17.0.7

LABEL authors="Andrey"

# Указываем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/*.jar app.jar

# Экспонируем порт, на котором работает приложение
EXPOSE 8089

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]

