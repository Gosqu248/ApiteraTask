package com.urban.atiperatask.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GithubControllerIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private static final String GITHUB_API_URL = "https://api.github.com";

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnOnlyNonForkRepositoriesWithBranches() throws Exception {
        // given
        var username = "testuser";

        var reposJson = """
            [
              {"name": "repo1", "owner":{"login": "testuser"}, "fork": false},
              {"name": "forked", "owner":{"login": "testuser"}, "fork": true}
            ]
            """;
        mockServer.expect(requestTo(String.format("%s/users/%s/repos", GITHUB_API_URL, username)))
                .andExpect(method(GET))
                .andRespond(withSuccess(reposJson, MediaType.APPLICATION_JSON));

        var branchesJson = """
            [
              {"name": "main", "commit": {"sha": "abc123"}},
              {"name": "dev", "commit": {"sha": "def456"}}
            ]
            """;
        mockServer.expect(requestTo(String.format("%s/repos/%s/%s/branches", GITHUB_API_URL, username, "repo1")))
                .andExpect(method(GET))
                .andRespond(withSuccess(branchesJson, MediaType.APPLICATION_JSON));

        // when
        var resultActions = mockMvc.perform(get("/api/github/{username}", username));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].repoName").value("repo1"))
                .andExpect(jsonPath("$[0].ownerLogin").value("testuser"))
                .andExpect(jsonPath("$[0].branches.length()").value(2))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("abc123"))
                .andExpect(jsonPath("$[0].branches[1].name").value("dev"))
                .andExpect(jsonPath("$[0].branches[1].lastCommitSha").value("def456"));

        mockServer.verify();
    }
}
