package com.voting.kura.controller;

import com.voting.kura.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/{positionId}")
    public ResponseEntity<Map<String, Object>> getResultsByPosition(@PathVariable Long positionId) {
        return ResponseEntity.ok(resultService.getResultsByPosition(positionId));
    }
}