package three.consulting.epoc.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it.mvcMatchers("/docs", "/docs/**", "/docs-ui.html", "/swagger-ui/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer().jwt()
        }
    }
}
