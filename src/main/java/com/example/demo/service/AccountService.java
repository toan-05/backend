package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.repository.PasswordResetTokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountResponse login(LoginRequest request);

    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse updateAccount(Long id, UpdateAccountRequest request);

    AccountResponse getAccount(Long id);

    Page<AccountResponse> getAccounts(Pageable pageable, String keyword);

    void deleteAccount(Long id);

    AccountResponse softDelete(long id);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);
}
