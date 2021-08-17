package cn.wghtstudio.insurance.service.entity;

public class LoginResponseBodyBuilder {
    private String token;
    private int id;
    private String username;

    public LoginResponseBodyBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseBodyBuilder withID(int id) {
        this.id = id;
        return this;
    }

    public LoginResponseBodyBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginResponseBody build() {
        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setUsername(username);
        loginResponseBody.setId(id);
        loginResponseBody.setToken(token);
        return loginResponseBody;
    }

    public static LoginResponseBodyBuilder getInstance() {
        return new LoginResponseBodyBuilder();
    }
}
