package com.example.demo.controller;

import com.example.demo.dto.request.ChangePasswordRequest;
import com.example.demo.dto.request.CreateAccountRequest;
import com.example.demo.dto.request.ForgotPasswordRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.ResetPasswordRequest;
import com.example.demo.dto.request.UpdateAccountRequest;
import com.example.demo.dto.response.AccountResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccountResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(accountService.login(request), "Đăng nhập thành công"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponse>> create(
            @RequestBody
            @Valid CreateAccountRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                accountService.createAccount(request), "Tạo tài khoản thành công"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponse>> update(
            @PathVariable long id, @RequestBody @Valid UpdateAccountRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                accountService.updateAccount(id, request), "Cập nhật tài khoản thành công"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<AccountResponse>> getDetail(
            @PathVariable long id) {
        return ResponseEntity.ok(ApiResponse.success(
                accountService.getAccount(id), "Lấy thông tin tài khoản thành công"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(
                accountService.getAccounts(pageable, keyword), "Lấy danh sách tài khoản thành công"));
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        accountService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đổi mật khẩu thành công"));
    }

    @PostMapping("/{id}/soft-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AccountResponse>> softDelete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                accountService.softDelete(id), "Xóa mềm tài khoản thành công"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        accountService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(null, "Đã gửi yêu cầu đặt lại mật khẩu"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        accountService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Đặt lại mật khẩu thành công"));
    }
}
