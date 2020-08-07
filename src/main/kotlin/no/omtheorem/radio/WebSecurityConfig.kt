package no.omtheorem.radio

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
internal class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth
                ?.inMemoryAuthentication()
                ?.withUser("user")
                ?.password(passwordEncoder().encode("password123"))
                ?.roles("USER")
    }

    override fun configure(http: HttpSecurity?) {
        http {
            authorizeRequests {
                authorize("/**", hasAuthority("ROLE_USER"))
            }
            csrf {
                disable()
            }
            httpBasic {}
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}