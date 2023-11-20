package com.backend.testManagement.repository;

import com.backend.testManagement.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, String> {

}
