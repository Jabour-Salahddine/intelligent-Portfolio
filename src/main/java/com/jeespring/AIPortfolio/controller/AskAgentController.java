package com.jeespring.AIPortfolio.controller;

import com.jeespring.AIPortfolio.agent.AiAgent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AskAgentController {

    private AiAgent aiAgent;

    @GetMapping("/ask-agent")
    public Flux<String> chatStream(@RequestParam(defaultValue = "salut !") String prompt) {

        if  (aiAgent.chatStream(prompt) != null) {
           return aiAgent.chatStream(prompt);
        } else {
          return   Flux.just("Désolé, je n'ai pas pu traiter votre demande pour le moment.");
        }
    }
}
