package org.maxkey.surpass.web.api.controller;

import lombok.RequiredArgsConstructor;

import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.api.ApiPublishRecord;
import org.maxkey.surpass.persistence.service.ApiPublishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publish")
@RequiredArgsConstructor
public class ApiPublishController {

    private final ApiPublishService apiPublishService;

    @GetMapping("/api/{apiId}/history")
    public Message<List<ApiPublishRecord>> getPublishHistory(@PathVariable String apiId) {
        List<ApiPublishRecord> history = apiPublishService.getPublishHistory(apiId);
        return Message.ok(history);
    }

    @GetMapping("/api/{apiId}/latest")
    public Message<ApiPublishRecord> getLatestPublishRecord(@PathVariable String apiId) {
        return apiPublishService.getLatestPublishRecord(apiId)
                .map(Message::ok)
                .orElse(Message.failed("获取失败"));
    }

    @PostMapping("/api/{apiId}/version/{versionId}/publish")
    public ResponseEntity<Void> publishVersion(
            @PathVariable("apiId") String apiId,
            @PathVariable("versionId") String versionId,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String description) {

        apiPublishService.publishVersion(apiId, versionId, operator, description);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/{apiId}/version/{versionId}/offline")
    public ResponseEntity<Void> offlineVersion(
            @PathVariable("apiId") String apiId,
            @PathVariable("versionId") String versionId,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String description) {

        apiPublishService.offlineVersion(apiId, versionId, operator, description);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/{apiId}/version/{versionId}/submit")
    public ResponseEntity<Void> submitForReview(
            @PathVariable("apiId") String apiId,
            @PathVariable("versionId") String versionId) {

        apiPublishService.submitForReview(apiId, versionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/{apiId}/version/{versionId}/reject")
    public ResponseEntity<Void> rejectVersion(
            @PathVariable("apiId") String apiId,
            @PathVariable("versionId") String versionId,
            @RequestParam String reason) {

        apiPublishService.rejectVersion(apiId, versionId, reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/{apiId}/version/{versionId}/online")
    public ResponseEntity<Void> onlineVersion(
            @PathVariable("apiId") String apiId,
            @PathVariable("versionId") String versionId,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String description) {

        apiPublishService.onlineVersion(apiId, versionId, operator, description);
        return ResponseEntity.ok().build();
    }
}
