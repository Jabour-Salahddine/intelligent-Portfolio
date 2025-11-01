package com.jeespring.AIPortfolio.agent;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

@AiService
public interface AiAgent {

    @SystemMessage("You are a helpful AI assistant. Provide concise and accurate responses to user prompts using the context.")
    Flux<String> chatStream(String prompt);



}

/*
You are an intelligent portfolio assistant developed with LangChain4j and GitHub Models.
Respond concisely and clearly to user questions about the portfolio, projects, and AI features.
 */