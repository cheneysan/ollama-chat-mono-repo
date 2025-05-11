package dev.p3s.ollamachat.service;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.models.response.OllamaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OllamaServiceImpl implements OllamaService {

    private final OllamaAPI api;
    private final String model;

    public OllamaServiceImpl(@Value("${ollama.api.url}") String ollamaApiUrl, @Value("${ollama.model}") String ollamaModel) {
        this.api = new OllamaAPI(ollamaApiUrl);
        this.model = ollamaModel;
        boolean isConnected = this.api.ping();
        if (isConnected) {
            log.info("Successfully pinged Ollama at {}", ollamaApiUrl);
            try {
                log.info("Pulling model {}...", ollamaModel);
                api.pullModel(ollamaModel);
            } catch (Exception e) {
                log.error("Failed to pull model {}", ollamaModel, e);
            }
        } else {
            log.error("Failed to ping Ollama at {}", ollamaApiUrl);
        }
    }

    @Override
    public String sendMessage(String message) {
        try {
            OllamaResult result = api.generate(this.model, message, null);
            return result.getResponse();
        } catch (Exception e) {
            log.error("Error while sending message to Ollama API", e);
            return "I'm not talking to you right now...";
        }
    }
}
