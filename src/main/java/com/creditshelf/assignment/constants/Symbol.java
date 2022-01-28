package com.creditshelf.assignment.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Symbol {
    X(1), O(2);

    private Integer value;
}
