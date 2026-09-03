package com.example.demo.dto.request;

import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class UpdateAccountRequest {

    @Email(message = "email không đúng định dạng")
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String fullName;

    private Status status;

    private Role role;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
