package three.consulting.epoc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Profile("default")
@Component
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
class SecurityConfig() {

    private companion object {
        const val AUTHORITIES_CLAIM_NAME = "role"
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().authorizeRequests {
            it
                .mvcMatchers("/docs", "/docs/**", "/docs-ui.html", "/swagger-ui/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter())
        }
        return http.build()
    }

    protected fun authenticationConverter(): JwtAuthenticationConverter? {
        val authoritiesConverter = JwtGrantedAuthoritiesConverter()
        authoritiesConverter.setAuthorityPrefix("")
        authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME)
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter)
        return converter
    }
}

@Configuration
class CorsConfig {

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer =
        object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("*")
                    .allowCredentials(true)
                    .maxAge(3600)
            }
        }
}
