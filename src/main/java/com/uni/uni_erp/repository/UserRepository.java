package com.uni.uni_erp.repository;

import com.uni.uni_erp.domain.entity.User;
import com.uni.uni_erp.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmailAndPassword(String email, String password);

}
