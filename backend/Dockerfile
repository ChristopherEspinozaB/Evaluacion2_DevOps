# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia los archivos del Maven Wrapper
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Da permisos de ejecución
RUN chmod +x ./mvnw

# Descarga las dependencias
RUN ./mvnw dependency:go-offline -B

# Copia el código fuente
COPY src ./src

# Compila el proyecto
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]