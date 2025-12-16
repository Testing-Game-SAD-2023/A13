package com.example.t9profileservice.llm;

import com.example.t9profileservice.dto.PlayerBioData;

public interface LlmClient {
    String generateBio(PlayerBioData data);
}
