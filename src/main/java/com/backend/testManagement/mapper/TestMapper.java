package com.backend.testManagement.mapper;

import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;
import com.backend.testManagement.model.Test;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {

    public TestDTO mapToDTO(Test test) {
        return TestDTO.builder()
                .id(test.getId())
                .name(test.getName())
                .lastname(test.getLastname())
                .build();
    }

    public Test mapToEntity(TestDTOSave testDTO) {
        return Test.builder()
                .name(testDTO.getName())
                .lastname(testDTO.getLastname())
                .build();
    }
}