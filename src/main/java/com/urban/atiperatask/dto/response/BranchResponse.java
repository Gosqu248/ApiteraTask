package com.urban.atiperatask.dto.response;

import com.urban.atiperatask.model.Branch;

public record BranchResponse(
        String name,
        String lastCommitSha
) {
    public static BranchResponse fromBranch(Branch branch) {
        return new BranchResponse(
                branch.getName(),
                branch.getCommit().getSha()
        );
    }
}
