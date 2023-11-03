package com.backend.testManagement.services.Impl;

import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;
import com.backend.testManagement.exceptions.BadRequestException;
import com.backend.testManagement.exceptions.EntityNotFoundException;
import com.backend.testManagement.exceptions.InternalServerErrorException;
import com.backend.testManagement.model.Test;
import com.backend.testManagement.repository.TestRepository;
import com.backend.testManagement.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    private static final Logger logger = Logger.getLogger(TestServiceImpl.class.getName());

    private final TestRepository testRepository;

    @Autowired
    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }


    @Override
    @Transactional
    public TestDTO saveTest(TestDTOSave testDTO) {
        // Validate the input
        try {
            // Convert the DTO to a Test entity
            Test test = convertToEntity(testDTO);

            // Save the test entity to the repository
            Test savedTest = testRepository.save(test);

            // Convert the saved test entity to a DTO and return it
            return convertToDTO(savedTest);
        } catch (DataAccessException ex) {
            // Handle data access exceptions
            logAndThrowInternalServerError("Error saving test", ex);
            return null; // Unreachable code
        } catch (BadRequestException ex) {
            // Handle validation exceptions
            logAndThrowBadRequest("Invalid request: " + ex.getMessage());
            return null; // Unreachable code
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAllTests(int pageNo, int pageSize, String sortBy, String sortDirection) {

        // Validate the input parameters
        if (pageNo < 0) {
            throw new IllegalArgumentException("Page number must be non-negative");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        // Create a Pageable object
        Sort sort = Sort.by(sortBy).ascending();
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Retrieve the test page
        Page<Test> testPage = testRepository.findAll(pageable);

        // Check if there are any tests
        if (testPage.isEmpty()) {
            logAndThrowEntityNotFoundException("No tests found");
        }

        // Convert the Test objects to TestDTO objects
        List<TestDTO> testDTOs = testPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Create the response map
        Map<String, Object> response = new HashMap<>();
        response.put("list", testDTOs);
        response.put("totalItems", testPage.getTotalElements());
        response.put("currentPage", testPage.getNumber());
        response.put("pageNumber", pageNo);
        response.put("pageSize", pageSize);

        return response;

    }


    private void logAndThrowEntityNotFoundException(String message) {
        logger.warning(message);
        throw new EntityNotFoundException(message);
    }

    private void logAndThrowInternalServerError(String message, Exception ex) {
        logger.severe(message + ": " + ex.getMessage());
        throw new InternalServerErrorException(message);
    }

    private void logAndThrowBadRequest(String message) {
        logger.warning(message);
        throw new BadRequestException(message);
    }

    private TestDTO convertToDTO(Test test) {
        return TestDTO.builder()
                .id(test.getId())
                .name(test.getName())
                .lastname(test.getLastname())
                .build();
    }

    private Test convertToEntity(TestDTOSave testDTO) {
        return Test.builder()
                .name(testDTO.getName())
                .lastname(testDTO.getLastname())
                .build();
    }
}
