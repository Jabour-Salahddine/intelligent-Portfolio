package com.jeespring.AIPortfolio.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
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

    @Bean
    ChatMemoryProvider chatMemoryProvider(){
        // on a ajouter une mémoire de 12 messages à notre agent
        return chatId -> MessageWindowChatMemory.withMaxMessages(12);
    }






}