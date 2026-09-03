package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<Account> findByStatus(Status status, Pageable pageable);

    @Query("""
            SELECT a FROM Account a
            WHERE LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Account> searchByKeyword(@Param("keyword") String keyword,
                                  Pageable pageable);
}
