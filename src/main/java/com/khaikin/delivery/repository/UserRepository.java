package com.khaikin.delivery.repository;

import com.khaikin.delivery.entity.User;
import com.khaikin.delivery.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Page<User> findAllByRole(Role role, Pageable pageable);

//    @Query("SELECT u.username FROM User u WHERE u.role = :role")
//    List<String> findUsernamesByRole(@Param("role") Role role);
}
