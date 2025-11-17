# Estágio 1:
FROM maven:3.8.7-openjdk-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml para o container
COPY pom.xml .

# Baixa as dependências (para agilizar o próximo build)
RUN mvn dependency:go-offline

# Copia todo o código-fonte
COPY src ./src

# Compila o projeto e cria o arquivo JAR na pasta 'target'
RUN mvn clean install -DskipTests

# Usamos uma imagem menor, apenas com o ambiente de execução Java (JRE)
FROM openjdk:17-jre-slim

# Define a porta de exposição (Spring Boot padrão)
EXPOSE 8080

# Copia o arquivo JAR do estágio de build para o estágio de runtime
# O nome do JAR pode variar, o '*' pega o primeiro arquivo .jar que encontrar
COPY --from=build /app/target/*.jar app.jar

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]