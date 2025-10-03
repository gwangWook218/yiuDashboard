package com.yiuDashboard.controller;

import com.yiuDashboard.dto.TargetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roadmap")
public class TargetController {

    @PostMapping("/target")
    public ResponseEntity<?> createTarget(@RequestBody TargetRequest req) {
        // TODO: 서비스 호출
        return ResponseEntity.ok().build();
    }

    @GetMapping("/performance/summary")
    public ResponseEntity<?> summary(@RequestParam long studentId) {
        // TODO: 서비스 호출
        return ResponseEntity.ok().build();
    }
}
