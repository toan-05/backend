package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

    @NotBlank(message = "token không được bỏ trống")
    private String token;

    @NotBlank(message = "mật khẩu mới không được bỏ trống")
    @Size(min = 6, max = 100, message = "mật khẩu mới phải từ 6 ký tự")
    private String newPassword;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
