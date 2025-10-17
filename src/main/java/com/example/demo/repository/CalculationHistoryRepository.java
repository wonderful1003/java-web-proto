package com.example.demo.repository;

import com.example.demo.entity.CalculationHistory;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {
    List<CalculationHistory> findByUserOrderByCreatedAtDesc(User user);
    List<CalculationHistory> findByUser_UsernameOrderByCreatedAtDesc(String username);
}
