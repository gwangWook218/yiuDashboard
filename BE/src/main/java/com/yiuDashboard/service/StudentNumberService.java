package com.yiuDashboard.service;

import com.yiuDashboard.dto.StudentNumberDto;
import com.yiuDashboard.entity.StudentNumber;
import com.yiuDashboard.repository.StudentNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentNumberService {

    private final StudentNumberRepository repository;

    public List<StudentNumber> getAllByYear(int year) {
        return repository.findAllByYear(year);
    }

    public StudentNumber getOne(int year, int deptId) {
        return repository.findByYearAndDepartmentId(year, deptId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "데이터 없음: year=" + year + ", deptId=" + deptId));
    }

    public StudentNumberDto getOneDto(int year, int deptId) {
        StudentNumber s = repository.findByYearAndDepartmentId(year, deptId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "데이터 없음: year=%d, deptId=%d".formatted(year, deptId)));
        return StudentNumberDto.from(s);
    }

    public List<StudentNumber> getByCategory(String majorCategory) {
        return repository.findByMajorCategory(majorCategory);
    }

    public StudentNumber save(StudentNumber body) {
        return repository.save(body);
    }
}
