package com.backend.testManagement.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponseDTO<T> {
    private List<T> list;
    private long totalItems;
    private int currentPage;
    private int pageNumber;
    private int pageSize;

}