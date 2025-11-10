# --- Etapa 1: Compilación (Build) ---
# Usamos la imagen oficial de Eclipse Temurin con JDK 17 y la nombramos 'build'
FROM eclipse-temurin:17-jdk-jammy AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /workspace

# Copiamos los archivos de Maven (si usas Maven)
# Esto optimiza el caché de capas de Docker.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargamos las dependencias de Maven
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos el proyecto y generamos el .jar
# Saltamos los tests para agilizar el build de Docker
RUN ./mvnw package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen base mucho más ligera, solo con el JRE 17
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Puerto por defecto de Spring Boot
EXPOSE 8080

# Si tu .jar tiene otro nombre, ajusta la línea de abajo.
COPY --from=build /workspace/target/*.jar app.jar

# El comando para arrancar la aplicación cuando el contenedor inicie
ENTRYPOINT ["java", "-jar", "app.jar"]