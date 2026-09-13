package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.GigDescriptionRequestDto;
import com.Project.TalentConnect.DTO.GigDescriptionResponseDto;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ExternalServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String MODEL = "gemini-3.6-flash";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL
            + ":generateContent";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GigDescriptionResponseDto generateDescription(GigDescriptionRequestDto request) {

        String prompt = """
                Write a professional, engaging freelance gig description (3-4 sentences, no headings) for:
                Title: %s
                Category: %s
                Skills: %s
                """.formatted(request.getTitle(), request.getCategory(), request.getSkills());

        String requestBody = """
                {
                  "contents": [{
                    "parts": [{"text": %s}]
                  }]
                }
                """.formatted(objectMapper.valueToTree(prompt).toString());

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_URL + "?key=" + geminiApiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("Gemini API error (status " + response.statusCode() + "): " + response.body());
                throw new ExternalServiceException("AI service is temporarily unavailable. Please try again later.");
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode candidates = root.path("candidates");

            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new BadRequestException("AI service returned no result — the prompt may have been blocked.");
            }

            JsonNode parts = candidates.get(0).path("content").path("parts");

            if (!parts.isArray() || parts.isEmpty()) {
                throw new BadRequestException("AI service returned no result — the prompt may have been blocked.");
            }

            JsonNode textNode = parts.get(0).path("text");

            if (textNode.isMissingNode()) {
                throw new BadRequestException("AI service returned no result — the prompt may have been blocked.");
            }

            String text = textNode.asText();

            return new GigDescriptionResponseDto(text.trim());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExternalServiceException("Failed to reach AI service: " + e.getMessage());
        } catch (java.io.IOException e) {
            throw new ExternalServiceException("Failed to reach AI service: " + e.getMessage());
        }
    }
}
