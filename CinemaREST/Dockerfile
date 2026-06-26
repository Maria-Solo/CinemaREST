# Шаг 1: Берем базовый образ с Java (например, Eclipse Temurin OpenJDK 21)
FROM eclipse-temurin:21-jre-alpine

# Шаг 2: Указываем рабочую папку внутри контейнера
WORKDIR /app

# Шаг 3: Копируем ваш собранный .jar файл в контейнер
# (Замените target/*.jar на ваш путь, например build/libs/*.jar для Gradle)
COPY CinemaREST-0.0.1-SNAPSHOT.jar app.jar

# Шаг 4: Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
