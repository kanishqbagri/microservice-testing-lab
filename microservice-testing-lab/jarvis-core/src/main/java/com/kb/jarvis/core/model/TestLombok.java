package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestLombok {
    private String name;

    public static void main(String[] args) {
        TestLombok t = TestLombok.builder().name("Check").build();
        System.out.println(t);
    }

}
