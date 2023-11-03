package com.backend.testManagement.services;

import com.backend.testManagement.dto.CommonResponseDTO;
import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;

import java.util.List;
import java.util.Map;

public interface TestService {
    CommonResponseDTO<TestDTO> getAllTests(int pageNo, int pageSize, String sortBy, String sortDirection);
    TestDTO saveTest(TestDTOSave testDTO);
}
