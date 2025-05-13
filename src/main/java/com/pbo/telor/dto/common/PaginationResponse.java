package com.pbo.telor.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse {
    private int currentPage;
    private int totalPage;
    private int limit;
    private long totalItem;
}


