package com.example.demo.mapper;

import com.example.demo.dto.response.AccountResponse;
import com.example.demo.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getFullName(),
                account.getStatus(),
                account.getRole(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
