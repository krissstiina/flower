# Используем легковесный образ JRE 21 на базе Alpine Linux
FROM eclipse-temurin:21-jre-alpine

# Создаем рабочую директорию
WORKDIR /app

# Устанавливаем wget для healthcheck
RUN apk add --no-cache wget

# Создаем пользователя 'javauser', чтобы не запускать приложение от root (Security Best Practice)
RUN addgroup -S javauser && adduser -S javauser -G javauser

# Аргумент для поиска JAR файла
ARG JAR_FILE=target/*.jar

# Копируем JAR внутрь образа под именем app.jar
COPY ${JAR_FILE} app.jar

# Меняем владельца файла
RUN chown javauser:javauser /app/app.jar

# Переключаемся на пользователя
USER javauser

# Документируем порт
EXPOSE 8083

# Запускаем приложение с оптимизацией для контейнера
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
