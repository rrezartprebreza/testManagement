package com.backend.testManagement.controller;

import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;
import com.backend.testManagement.services.TestService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
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
    public ResponseEntity<Map<String, Object>> getAllTests(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                                                 @RequestParam(value = "sort", defaultValue = "id") String sortProperty,
                                                                 @RequestParam(value = "order", defaultValue = "asc") String order) {
        return new ResponseEntity<>(testService.getAllTests(page, size, sortProperty, order), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Test created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<TestDTO> saveTest(@RequestBody TestDTOSave testDTO) {

        TestDTO savedTestDTO = testService.saveTest(testDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTestDTO);

    }
}