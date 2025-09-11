package tn.esprit.spring.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(infoAPI())
                .servers(List.of(
                    new Server().url("http://localhost:8089/api").description("Development Server"),
                    new Server().url("https://api.skistation.com").description("Production Server")
                ));
    }

    public Info infoAPI() {
        return new Info()
                .title("🏂 SKI STATION MANAGEMENT API 🚠")
                .description("""
                    A comprehensive REST API for managing a ski station with the following features:
                    
                    **Core Entities:**
                    - **Skiers**: Manage skier profiles, subscriptions, and course registrations
                    - **Courses**: Handle different types of skiing courses (individual, collective)
                    - **Instructors**: Manage instructor profiles and course assignments
                    - **Pistes**: Track ski slopes with different difficulty levels
                    - **Registrations**: Handle course registrations and scheduling
                    - **Subscriptions**: Manage different subscription types and periods
                    
                    **Key Features:**
                    - Complete CRUD operations for all entities
                    - Advanced business logic for course assignments
                    - Subscription management with date filtering
                    - Piste difficulty tracking (Green, Blue, Red, Black)
                    - Support for both Ski and Snowboard courses
                    - Registration management with week-based scheduling
                    
                    **API Version:** 1.0
                    **Base URL:** /api
                    """)
                .version("1.0.0")
                .contact(contactAPI())
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
    }

    public Contact contactAPI() {
        return new Contact()
                .name("TEAM ASI II")
                .email("ons.bensalah@esprit.tn")
                .url("https://www.linkedin.com/in/ons-ben-salah-24b73494/");
    }

    @Bean
    public GroupedOpenApi skierApi() {
        return GroupedOpenApi.builder()
                .group("Skiers Management")
                .pathsToMatch("/skier/**")
                .build();
    }

    @Bean
    public GroupedOpenApi courseApi() {
        return GroupedOpenApi.builder()
                .group("Courses Management")
                .pathsToMatch("/course/**")
                .build();
    }

    @Bean
    public GroupedOpenApi instructorApi() {
        return GroupedOpenApi.builder()
                .group("Instructors Management")
                .pathsToMatch("/instructor/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pisteApi() {
        return GroupedOpenApi.builder()
                .group("Pistes Management")
                .pathsToMatch("/piste/**")
                .build();
    }

    @Bean
    public GroupedOpenApi registrationApi() {
        return GroupedOpenApi.builder()
                .group("Registrations Management")
                .pathsToMatch("/registration/**")
                .build();
    }

    @Bean
    public GroupedOpenApi subscriptionApi() {
        return GroupedOpenApi.builder()
                .group("Subscriptions Management")
                .pathsToMatch("/subscription/**")
                .build();
    }
}