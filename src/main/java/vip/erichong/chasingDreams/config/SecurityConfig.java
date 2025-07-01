package vip.erichong.chasingDreams.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vip.erichong.chasingDreams.security.*;

/**
 * @author eric
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CaptchaFilter captchaFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final JwtAuthenticationEntryPointImpl jwtAuthenticationEntryPointImpl;
    private final JwtAccessDeniedHandlerImpl jwtAccessDeniedHandlerImpl;

    private static final String[] URL_WHITELIST = {
            "/login",
            "/favicon.ico",
            "/captcha*",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 直接获取默认管理器, Spring 会自己管理
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        // 登录表单提交的URL（保持默认）
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        // 允许所有用户访问登录链接
                        .permitAll()
                )
                .logout(AbstractHttpConfigurer::disable)
                // 不通过 Session 获取 SecurityContext
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http
                .authorizeHttpRequests(authorize -> authorize
                        // 白名单路径
                        .requestMatchers(URL_WHITELIST).permitAll()
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                );

        // 配置自定义的过滤器
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter, JwtAuthenticationFilter.class);

        // 异常处理器
        http
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(jwtAuthenticationEntryPointImpl)
                                .accessDeniedHandler(jwtAccessDeniedHandlerImpl)
                );

        // Filter -> Security -> Spring MVC ( WebMvcConfigurer.addCorsMappings )
        // 如果使用 WebMvcConfigurer 的跨域处理，则需要 http.cors() 需要启用，让 Security 自己去委托 MVC 来处理跨域

        // 允许跨域, 由于 CorsConfig 中已经自定义了 CorsFilter
        // 此时应该显式的告诉 Security 不使用它自带的，用我们自定义的
        // 虽然 Security 会检查用户是否自定义了 CorsFilter，有定义时它会跳过不会重复创建默认的 CorsFilter
        // 但我们依然推荐你当自定义了 CorsFilter 则主动显式地配置它为禁用
        http.cors(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

