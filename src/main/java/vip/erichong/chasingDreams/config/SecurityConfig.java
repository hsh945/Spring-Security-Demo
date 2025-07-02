package vip.erichong.chasingDreams.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vip.erichong.chasingDreams.security.*;

import java.util.Arrays;

/**
 * @author eric
 */
@Configuration
@RequiredArgsConstructor
// 添加 EnableMethodSecurity 注解之后才能开启方法级权限控制
// 例如：UserController 中 方法上的 @PreAuthorize("hasAuthority('system:user:list')") 才会生效
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CaptchaFilter captchaFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final JwtAuthenticationEntryPointImpl jwtAuthenticationEntryPointImpl;
    private final JwtAccessDeniedHandlerImpl jwtAccessDeniedHandlerImpl;

    private static final String[] URL_WHITELIST = {
            "/favicon.ico",
            "/captcha",
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 1. 允许的来源（生产环境必须指定具体域名，如 "http://localhost:8080"）
        // 允许所有来源（开发环境用）
        config.addAllowedOriginPattern("*");
        // 2. 允许的请求方法（必须包含 OPTIONS，预检请求用）
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 3. 允许的请求头（包含自定义头，如 Token）
        config.addAllowedHeader("*");
        // 4. 允许携带 Cookie（跨域登录需要时开启）
        config.setAllowCredentials(true);
        // 5. 预检请求的缓存时间（减少 OPTIONS 请求次数）
        config.setMaxAge(3600L);
        // 6. 对所有接口生效
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

