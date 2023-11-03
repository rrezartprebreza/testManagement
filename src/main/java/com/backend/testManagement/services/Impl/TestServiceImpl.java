package com.backend.testManagement.services.Impl;

import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.exceptions.BadRequestException;
import com.backend.testManagement.exceptions.EntityNotFoundException;
import com.backend.testManagement.exceptions.InternalServerErrorException;
import com.backend.testManagement.model.TestModel;
import com.backend.testManagement.repository.TestRepository;
import com.backend.testManagement.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional(readOnly = true)
    public List<TestDTO> getAllTests() {
        logger.info("Retrieving all tests");

        try {
            List<TestDTO> tests = testRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            if (tests.isEmpty()) {
                logAndThrowEntityNotFoundException("No tests found");
            }

            logger.info("Successfully retrieved all tests");
            return tests;
        } catch (DataAccessException ex) {
            logAndThrowInternalServerError("Error retrieving tests", ex);
        } catch (BadRequestException ex) {
            logAndThrowBadRequest("Invalid request: " + ex.getMessage());
        }
        return null;
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

    private TestDTO convertToDTO(TestModel testModel) {
        return TestDTO.builder()
                .id(testModel.getId())
                .name(testModel.getName())
                .lastname(testModel.getLastname())
                .build();
    }
}
