package com.urban.atiperatask.excetion;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GithubUserNotFoundException extends RuntimeException {
    private final String msg;
}

