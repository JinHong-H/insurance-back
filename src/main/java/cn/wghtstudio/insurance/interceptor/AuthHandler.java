package cn.wghtstudio.insurance.interceptor;

import cn.wghtstudio.insurance.dao.repository.UserRepository;
import cn.wghtstudio.insurance.util.Result;
import cn.wghtstudio.insurance.util.ResultEnum;
import cn.wghtstudio.insurance.util.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthHandler implements HandlerInterceptor {
    UserRepository userRepository;
    Token token;

    public AuthHandler(UserRepository userRepository, Token token) {
        this.userRepository = userRepository;
        this.token = token;
    }

    private String getAuthorizationFromHeader(HttpServletRequest request) {
        final String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("insurance ")) {
            return null;
        }

        final String[] tokenArray = token.split(" ");
        if (tokenArray.length < 2) {
            return null;
        }

        return tokenArray[1];
    }

    private void parseTokenError(HttpServletResponse response) throws IOException {
        response.setStatus(403);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        printWriter.append(mapper.writeValueAsString(Result.error(ResultEnum.PARSE_TOKEN_ERROR)));
        printWriter.flush();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String tokenString = getAuthorizationFromHeader(request);
        if (tokenString == null) {
            parseTokenError(response);
            return false;
        }

        int id;

        try {
            id = token.verify(tokenString);
        } catch (Exception e) {
            parseTokenError(response);
            return false;
        }

        return true;
    }
}
