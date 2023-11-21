package com.backend.testManagement.services.Impl;

import com.backend.testManagement.dto.CommonResponseDTO;
import com.backend.testManagement.dto.TestDTO;
import com.backend.testManagement.dto.TestDTOSave;
import com.backend.testManagement.dto.ValidationUtilsDTO;
import com.backend.testManagement.exceptions.BadRequestException;
import com.backend.testManagement.exceptions.EntityNotFoundException;
import com.backend.testManagement.exceptions.InternalServerErrorException;
import com.backend.testManagement.mapper.TestMapper;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    private static final Logger logger = Logger.getLogger(TestServiceImpl.class.getName());

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    @Autowired
    public TestServiceImpl(TestRepository testRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
    }

    @Override
    @Transactional
    public TestDTO saveTest(TestDTOSave testDTO) {
        try {
            Test test = testMapper.mapToEntity(testDTO);
            Test savedTest = testRepository.save(test);
            return testMapper.mapToDTO(savedTest);
        } catch (DataAccessException ex) {
            logAndThrowInternalServerError("Error saving test", ex);
            return null;
        } catch (BadRequestException ex) {
            logAndThrowBadRequest("Invalid request: " + ex.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public TestDTO updateTest(String id, TestDTOSave testDTO) {
        Test existingTest = findTestById(id);
        existingTest.setName(testDTO.getName());
        existingTest.setLastname(testDTO.getLastname());
        Test updatedTest = testRepository.save(existingTest);
        logger.info("Test updated successfully: " + id);
        return testMapper.mapToDTO(updatedTest);
    }


    @Override
    @Transactional
    public Test deleteTest(String id) {
        try {
            // Find the existing test by id
            Test existingTest = findTestById(id);

            // Delete the test from the repository
            testRepository.delete(existingTest);

            // Log the successful deletion
            logger.info("Test deleted successfully: " + id);
            return existingTest;
        } catch (DataAccessException ex) {
            logAndThrowInternalServerError("Error deleting test", ex);
        } catch (EntityNotFoundException ex) {
            logAndThrowEntityNotFoundException("Test not found: " + id);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonResponseDTO<TestDTO> getAllTests(int pageNo, int pageSize, String sortBy, String sortDirection) {
        ValidationUtilsDTO.validatePageParameters(pageNo, pageSize);
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Test> testPage = testRepository.findAll(pageable);
        if (testPage.isEmpty()) {
            logAndThrowEntityNotFoundException("No tests found");
        }
        List<TestDTO> testDTOs = testPage.getContent().stream()
                .map(testMapper::mapToDTO)
                .collect(Collectors.toList());
        return buildCommonResponse(testDTOs, testPage);
    }

    @Override
    public Test findTestById(String id) {
        return testRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Test not found: " + id);
                    return new EntityNotFoundException("Test not found");
                });
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

    private CommonResponseDTO<TestDTO> buildCommonResponse(List<TestDTO> testDTOs, Page<Test> testPage) {

        return CommonResponseDTO.<TestDTO>builder()
                .list(testDTOs)
                .totalItems(testPage.getTotalElements())
                .currentPage(testPage.getNumber())
                .pageNumber(testPage.getNumber())
                .pageSize(testPage.getSize())
                .build();

    }

}
