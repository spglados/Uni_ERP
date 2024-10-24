package com.uni.uni_erp.repository.user;

import com.uni.uni_erp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmailAndPassword(String email, String password);

}
