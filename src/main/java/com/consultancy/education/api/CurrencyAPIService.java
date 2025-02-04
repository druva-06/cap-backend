package com.consultancy.education.api;

import com.consultancy.education.DTOs.responseDTOs.currency.CurrencyResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CurrencyAPIService {
    private final WebClient webClient;

    public CurrencyAPIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://v6.exchangerate-api.com").build();
    }

    public Mono<CurrencyResponseDTO> fetchData() {
        return webClient.get()
                .uri("/v6/741478df614e4e029fc56bc9/latest/INR")
                .retrieve()
                .bodyToMono(CurrencyResponseDTO.class);
    }
}
