package com.example.demo.service.impl;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.enums.Role;
import com.example.demo.entity.enums.Status;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final SecureRandom random = new SecureRandom();

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public AccountResponse login(LoginRequest request) {
        Account account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        if (account.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Tài khoản đã bị vô hiệu hóa");
        }
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
        return accountMapper.toResponse(account);
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("username đã tồn tại");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email đã tồn tại");
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setFullName(request.getFullName());
        account.setStatus(request.getStatus() != null ? request.getStatus() : Status.ACTIVE);
        account.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        account = accountRepository.save(account);

        return accountMapper.toResponse(account);
    }

    @Override
    public AccountResponse updateAccount(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        if (request.getEmail() != null && !request.getEmail().equals(account.getEmail()) && accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (request.getEmail() != null) {
            account.setEmail(request.getEmail());
        }
        if (request.getFullName() != null) {
            account.setFullName(request.getFullName());
        }
        if (request.getStatus() != null) {
            account.setStatus(request.getStatus());
        }
        if (request.getRole() != null) {
            account.setRole(request.getRole());
        }

        Account save = accountRepository.save(account);
        return accountMapper.toResponse(save);
    }

    @Override
    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin"));

        return accountMapper.toResponse(account);
    }

    @Override
    public Page<AccountResponse> getAccounts(Pageable pageable, String keyword) {
        Page<Account> page;
        if (keyword == null || keyword.isBlank()) {
            page = accountRepository.findAll(pageable);
        } else {
            page = accountRepository.searchByKeyword(keyword, pageable);
        }

        return page.map(accountMapper::toResponse);
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("không tìm thấy tài khoản");
        }

        accountRepository.deleteById(id);
    }

    @Override
    public AccountResponse softDelete(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy tài khoản"));
        account.setStatus(Status.INACTIVE);
        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("không tìm thấy tài khoản"));
        if (request.getNewPassword().equals(request.getOldPassword())) {
            throw  new RuntimeException("không được trùng mật khẩu cũ");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new RuntimeException("mật khẩu cũ không đúng");
        }
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy email"));
        passwordResetTokenRepository.deleteByAccount(account);
        String token;
        do {
            token = String.valueOf(100000 + random.nextInt(900000));
        } while (passwordResetTokenRepository.findByToken(token).isPresent());
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setAccount(account);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(passwordResetToken);
        emailService.sendResetToken(account.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn");
        }
        Account account = passwordResetToken.getAccount();
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
