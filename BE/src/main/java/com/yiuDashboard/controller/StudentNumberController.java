package com.yiuDashboard.controller;

import com.yiuDashboard.dto.StudentNumberDto;
import com.yiuDashboard.entity.StudentNumber;
import com.yiuDashboard.service.StudentNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-numbers")
@RequiredArgsConstructor
public class StudentNumberController {

    private final StudentNumberService service;

    @GetMapping
    public ResponseEntity<List<StudentNumber>> getAllByYear(@RequestParam int year) {
        return ResponseEntity.ok(service.getAllByYear(year));
    }

    @GetMapping("/one")
    public ResponseEntity<StudentNumberDto> getOne(
            @RequestParam int year, @RequestParam int deptId) {
        return ResponseEntity.ok(service.getOneDto(year, deptId));
    }

    @GetMapping("/category/{majorCategory}")
    public ResponseEntity<List<StudentNumber>> byCategory(@PathVariable String majorCategory) {
        return ResponseEntity.ok(service.getByCategory(majorCategory));
    }

    @PostMapping
    public ResponseEntity<StudentNumber> save(@RequestBody StudentNumber body) {
        return ResponseEntity.ok(service.save(body));
    }
}
