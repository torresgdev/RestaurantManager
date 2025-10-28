package com.restaurant.ez_rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Ez-Restaurant API - Sistema de Pedidos").version("v1.0.0")
                .description("API REST para gestão de pedidos em restaurante."
                +"Inclui CRUD de Cardápio/mesas e o ciclo de vida da Comanda"));
    }
}
