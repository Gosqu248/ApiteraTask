package com.urban.atiperatask.controller;

import com.urban.atiperatask.dto.response.RepositoryResponse;
import com.urban.atiperatask.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {
    private final GitHubService githubService;

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryResponse>> getRepositories(@PathVariable String username) {
        return ResponseEntity.ok(githubService.getRepositories(username));
    }
}
