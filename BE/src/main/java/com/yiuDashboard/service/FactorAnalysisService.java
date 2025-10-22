package com.yiuDashboard.service;

import com.yiuDashboard.entity.factorAnalysis.FactorAnalysis;
import com.yiuDashboard.repository.FactorAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FactorAnalysisService {

    private final FactorAnalysisRepository repository;

    public List<Map<String, Object>> findByYearAndType(int year, String type) {
        List<Object[]> results = repository.findByYearAndType(year, type);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("factorName", row[0]);
            map.put("value", row[1]);
            response.add(map);
        }

        return response;
    }
}
