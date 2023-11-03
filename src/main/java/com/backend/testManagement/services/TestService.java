package com.backend.testManagement.services;

import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;

import java.util.List;
import java.util.Map;

public interface TestService {
    Map<String, Object> getAllTests(int pageNo, int pageSize, String sortBy, String sortDirection);
    TestDTO saveTest(TestDTOSave testDTO);
}
