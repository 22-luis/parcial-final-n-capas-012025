# Usar una imagen oficial de OpenJDK para Java 21
FROM eclipse-temurin:21-jdk as build

WORKDIR /app

# Copiar el archivo pom y descargar dependencias
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copiar el resto del código fuente
COPY src ./src

# Construir el jar
RUN ./mvnw package -DskipTests

# Segunda etapa: imagen más liviana solo con el jar
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"] 