package com.masai.repository;

import com.masai.model.LoginDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository  extends JpaRepository<LoginDto, Long> {
    Optional<LoginDto> findByEmail(String email); //loginDTO is user
}
