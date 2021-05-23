package sogon.booksys.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sogon.booksys.domain.Role;
import sogon.booksys.service.CustomOAuth2UserService;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/login","/restaurant/Hi","/restaurant/introduce",
                        "/restaurant/location","/menu/lunch","/menu/dinner","/membership/card",
                        "/customercenter/center","/membership/event", "/css/**","/js/**","/image/**").permitAll()
                .antMatchers("/tables/**","/users").hasRole(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }
}
