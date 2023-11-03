package com.backend.testManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO<T> {
    private List<T> list;
    private long totalItems;
    private int currentPage;
    private int pageNumber;
    private int pageSize;

}