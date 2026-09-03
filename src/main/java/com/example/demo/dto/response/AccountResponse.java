package com.example.demo.dto.response;

import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.Status;

import java.time.LocalDateTime;

public class AccountResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Status status;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountResponse(Long id, String username, String email, String fullName, Status status, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Status getStatus() {
        return status;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
