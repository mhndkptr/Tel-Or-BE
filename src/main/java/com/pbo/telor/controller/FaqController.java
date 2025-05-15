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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<FaqResponse>>> getFaqsPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        Page<FaqResponse> faqs = faqService.findAllPaged(page, limit);

        return ResponseUtil.paged(
                faqs.getContent(),
                PaginationResponse.builder()
                        .currentPage(faqs.getNumber() + 1)
                        .totalPage(faqs.getTotalPages())
                        .totalItem(faqs.getTotalElements())
                        .limit(faqs.getSize())
                        .build(),
                "Paged FAQs fetched successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<FaqResponse>> getFaqById(@PathVariable UUID id) {
        FaqResponse faq = faqService.getFaqById(id);
        return ResponseUtil.ok(faq, "Successfully retrieved FAQ");
    }

    @PostMapping
    public ResponseEntity<BaseResponse<FaqResponse>> createFaq(@RequestBody FaqRequest request) {
        FaqResponse faq = faqService.createFaq(request);
        return ResponseUtil.ok(faq, "Successfully created FAQ");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<FaqResponse>> updateFaq(
            @PathVariable UUID id,
            @RequestBody FaqRequest request) {
        FaqResponse faq = faqService.updateFaq(id, request);
        return ResponseUtil.ok(faq, "Successfully updated FAQ");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<FaqResponse>> patchFaq(
            @PathVariable UUID id,
            @RequestBody FaqRequest patchData) {
        FaqResponse faq = faqService.patchFaq(id, patchData);
        return ResponseUtil.ok(faq, "Successfully patched FAQ");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteFaq(@PathVariable UUID id) {
        faqService.deleteFaq(id);
        return ResponseUtil.ok(null, "Successfully deleted FAQ");
    }
}