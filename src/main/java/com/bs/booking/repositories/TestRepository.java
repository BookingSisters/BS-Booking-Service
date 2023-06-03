package com.bs.booking.repositories;

import com.bs.booking.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findByAgeAndName(Integer age, String name);
    Test findByTestId(Long testId);
}
