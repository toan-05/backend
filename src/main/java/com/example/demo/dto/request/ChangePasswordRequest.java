package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "mật khẩu cũ không được bỏ trống")
    private String oldPassword;

    @NotBlank(message = "mật khẩu mới không được bỏ trống")
    @Size(min = 6, max = 100, message = "mật khẩu mới phải từ 6 ký tự")
    private String newPassword;


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
