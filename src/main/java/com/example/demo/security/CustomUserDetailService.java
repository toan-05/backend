package com.example.demo.security;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Status;
import com.example.demo.repository.AccountRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
        if (Status.INACTIVE.equals(account.getStatus())) {
            throw new DisabledException("Tài khoản đã bị vô hiệu hóa");
        }
        if(account.getRole()==null)
            throw new DisabledException("Chưa gán role");
        return new User(account.getUsername(), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name())));
    }
}
