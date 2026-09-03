package com.example.demo.dto.request;

import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateAccountRequest {

    @NotBlank(message = "username không được bỏ trống")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "email không được bỏ trống")
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank(message = "password không được bỏ trống")
    @Size(min = 6, max = 100)
    private String password;

    @Size(max = 100)
    private String fullName;

    private Status status;

    private Role role;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
