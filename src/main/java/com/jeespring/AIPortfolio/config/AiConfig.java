package com.jeespring.AIPortfolio.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiConfig {

    private static final String API_KEY = System.getenv("exemple_api"); // Ton token GitHub PAT
    private static final String BASE_URL = "https://models.inference.ai.azure.com";
    private static final String MODEL_NAME = "gpt-4o";

    /** Modèle classique (non streaming) */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .temperature(0.2)
                .build();
    }

    /**  Modèle streaming (pour Flux<String>) */
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .temperature(0.2)
                .build();
    }
}