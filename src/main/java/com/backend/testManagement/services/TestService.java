package com.backend.testManagement.services;

import com.backend.testManagement.dto.TestDTO;

import java.util.List;

public interface TestService {
    List<TestDTO> getAllTests();
}
