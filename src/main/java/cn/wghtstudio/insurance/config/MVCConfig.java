package cn.wghtstudio.insurance.config;

import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.interceptor.AuthHandler;
import cn.wghtstudio.insurance.util.Token;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Component
@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Resource
    UserRepository userRepository;

    @Resource
    Token token;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthHandler(userRepository, token)).
                addPathPatterns("/**").
                excludePathPatterns("/login");
    }
}
