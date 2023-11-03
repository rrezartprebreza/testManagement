package com.backend.testManagement.repository;

import com.backend.testManagement.model.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestModel, String> {
}
