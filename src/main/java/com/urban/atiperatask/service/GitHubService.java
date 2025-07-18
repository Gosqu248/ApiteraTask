package com.urban.atiperatask.service;

import com.urban.atiperatask.dto.response.BranchResponse;
import com.urban.atiperatask.dto.response.RepositoryResponse;
import com.urban.atiperatask.excetion.GithubUserNotFoundException;
import com.urban.atiperatask.model.Branch;
import com.urban.atiperatask.model.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GitHubService {
    private static final String GITHUB_URL = "https://api.github.com";

    private final RestTemplate restTemplate;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-]{1,39}$");

    public List<RepositoryResponse> getRepositories(String username) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Invalid GitHub username");
        }

        try {
            List<Repository> repos = fetchRepos(username);

            return repos.stream()
                    .filter(repo -> !repo.isFork())
                    .map(repo -> {
                        List<BranchResponse> branches = fetchBranches(repo.getOwner().getLogin(), repo.getName());
                        return new RepositoryResponse(
                                repo.getName(),
                                repo.getOwner().getLogin(),
                                branches
                        );
                    })
                    .toList();

        } catch (HttpClientErrorException.NotFound ex) {
            throw new GithubUserNotFoundException("User not found on GitHub");
        }
    }

    private List<Repository> fetchRepos(String username) {
        String url = String.format("%s/users/%s/repos", GITHUB_URL, username);
        return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Repository>>() {}
                ).getBody();
    }

    private List<BranchResponse> fetchBranches(String owner, String repoName) {
        String url = String.format("%s/repos/%s/%s/branches",GITHUB_URL, owner, repoName);
        List<Branch> branches = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Branch>>() {}
        ).getBody();

        if (branches == null) {
            return List.of();
        }

        return branches.stream()
                .map(BranchResponse::fromBranch)
                .toList();
    }
}
