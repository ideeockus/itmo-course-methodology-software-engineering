# Используйте официальный образ Gradle для сборки проекта
FROM gradle:jdk11 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Соберите исполняемый JAR файл
RUN gradle clean build

# Используйте образ OpenJDK для запуска вашего приложения
FROM openjdk:11-jre-slim

# Копируйте собранный JAR файл из предыдущего шага
COPY --from=builder /home/gradle/src/build/libs/com.memoryerasureservice-all.jar /app/application.jar
COPY src/main/resources/static /app/src/main/resources/static

# Задайте рабочую директорию
WORKDIR /app

# Запустите ваше приложение
CMD ["java", "-jar", "application.jar"]
