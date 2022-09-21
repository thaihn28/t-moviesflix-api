//package com.example.tmovierestapi.config;
//
//import io.swagger.v3.oas.annotations.servers.Server;
//import io.swagger.v3.oas.models.OpenAPI;
//import org.modelmapper.internal.util.Lists;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OpenApiConfig {
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                // Thiết lập các server dùng để test api
//                .servers(Lists.newArrayList(
//                        new Server().url("http://localhost:8080"),
//                        new Server().url("https://user.loda.me")
//                ))
//                // info
//                .info(new Info().title("Loda Application API")
//                        .description("Sample OpenAPI 3.0")
//                        .contact(new Contact()
//                                .email("loda.namnh@gmail.com")
//                                .name("loda")
//                                .url("https://loda.me/"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
//                        .version("1.0.0"));
//    }
//}
