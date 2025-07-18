package com.urban.atiperatask.dto.response;

import java.util.List;

public record RepositoryResponse(
        String repoName,
        String ownerLogin,
        List<BranchResponse> branches
) {
}
