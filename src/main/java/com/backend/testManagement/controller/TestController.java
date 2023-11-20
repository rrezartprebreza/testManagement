package com.backend.testManagement.controller;

import com.backend.testManagement.dto.CommonResponseDTO;
import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;
import com.backend.testManagement.exceptions.BadRequestException;
import com.backend.testManagement.exceptions.EntityNotFoundException;
import com.backend.testManagement.services.TestService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
@Validated
public class TestController {
    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all tests")
    @ApiResponse(responseCode = "404", description = "No tests found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    public ResponseEntity<CommonResponseDTO<TestDTO>> getAllTests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortProperty,
            @RequestParam(defaultValue = "asc") String order) {

        CommonResponseDTO<TestDTO> responseDTO = testService.getAllTests(page, size, sortProperty, order);
        return ResponseEntity.ok(responseDTO);

    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Test created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<TestDTO> saveTest(@RequestBody TestDTOSave testDTO) {

        TestDTO savedTestDTO = testService.saveTest(testDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTestDTO);

    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Test updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "404", description = "Test not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<TestDTO> updateTest(@PathVariable String id, @RequestBody TestDTOSave testDTO) {

        TestDTO updatedTestDTO = testService.updateTest(id, testDTO);
        return ResponseEntity.ok(updatedTestDTO);

    }
}