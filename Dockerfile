# ETAPA 1: Compilación (Build)
# Usamos Maven para construir el archivo .jar desde cero en el servidor
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copiamos todo tu código al contenedor
COPY . .
# Ejecutamos el comando de compilación de Maven
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Run)
# Usamos una imagen ligera de Java para correr la app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Traemos el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto estándar
EXPOSE 8080
# Arrancamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]