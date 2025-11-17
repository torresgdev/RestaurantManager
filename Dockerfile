# Estágio 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Instala o Maven dentro do container Temurin
# O Render (baseado em Linux) já tem o 'apt' (instalador de pacotes)
RUN apt-get update && apt-get install -y maven

# Copia o arquivo pom.xml para o container
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline

# Copia todo o código-fonte
COPY src ./src

# Compila o projeto e cria o arquivo JAR
RUN mvn clean install -DskipTests

# Estágio 2: Runtime (usando apenas o JRE para ser leve)
# Manteve-se o eclipse-temurin, que já se provou seguro
FROM eclipse-temurin:21-jre-jammy

# Define a porta de exposição
EXPOSE 8080

# Copia o arquivo JAR do estágio de build para o estágio de runtime
COPY --from=build /app/target/*.jar app.jar

# Comando para iniciar a aplicação Spring Boot
# Comando para iniciar a aplicação Spring Boot, garantindo que o Spring leia a variável DATABASE_URL
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dspring.datasource.url=${DATABASE_URL}", "-Dspring.datasource.username=${POSTGRES_USER}", "-Dspring.datasource.password=${POSTGRES_PASSWORD}", "-jar", "/app.jar"]