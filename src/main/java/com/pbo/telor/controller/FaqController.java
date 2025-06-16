package com.pbo.telor.controller;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.PaginationResponse;
import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.service.FaqService;
import com.pbo.telor.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {
        private final FaqService faqService;

        @Operation(summary = "Get FAQ Data")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved FAQ data"),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
        })
        @GetMapping
        public ResponseEntity<BaseResponse<List<FaqResponse>>> getFaqsPaged(
                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "category", required = false) String category) {

                Page<FaqResponse> faqs = faqService.findAllPaged(page, limit, category);

                return ResponseUtil.paged(
                                faqs.getContent(),
                                PaginationResponse.builder()
                                                .currentPage(faqs.getNumber() + 1)
                                                .totalPage(faqs.getTotalPages())
                                                .totalItem(faqs.getTotalElements())
                                                .limit(faqs.getSize())
                                                .build(),
                                "Paged FAQs fetched successfully");
        }

        @Operation(summary = "Get FAQ Data by ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved FAQ data"),
                        @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
        })
        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<FaqResponse>> getFaqById(@PathVariable UUID id) {
                FaqResponse faq = faqService.getFaqById(id);
                return ResponseUtil.ok(faq, "Successfully retrieved FAQ");
        }

        @Operation(summary = "Create a new FAQ")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Successfully created FAQ"),
                        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
        })
        @PostMapping
        public ResponseEntity<BaseResponse<FaqResponse>> createFaq(@RequestBody FaqRequest request) {
                FaqResponse faq = faqService.createFaq(request);
                return ResponseUtil.ok(faq, "Successfully created FAQ");
        }

        @Operation(summary = "Update an existing FAQ")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully updated FAQ"),
                        @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content()),
                        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
        })
        @PutMapping("/{id}")
        public ResponseEntity<BaseResponse<FaqResponse>> updateFaq(
                        @PathVariable UUID id,
                        @RequestBody FaqRequest request) {
                FaqResponse faq = faqService.updateFaq(id, request);
                return ResponseUtil.ok(faq, "Successfully updated FAQ");
        }

        @Operation(summary = "Delete an FAQ Data")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Successfully deleted FAQ"),
                        @ApiResponse(responseCode = "404", description = "FAQ not found", content = @Content()),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<BaseResponse<Object>> deleteFaq(@PathVariable UUID id) {
                faqService.deleteFaq(id);
                return ResponseUtil.ok(null, "Successfully deleted FAQ");
        }
}