package com.example.LatteListBack.Services;
import com.example.LatteListBack.DTOs.GeoApifyDTOs.GeoapifyResponse;
import com.example.LatteListBack.Mappers.GeoapifyCafeMapper;
import com.example.LatteListBack.Models.Cafe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GeoapifyService {

    @Value("${geoapify.base.url}")
    private String geoapifyBaseUrl;

    @Value("${geoapify.api.key}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;
    private final GeoapifyCafeMapper mapper;

    public GeoapifyService(WebClient.Builder webClientBuilder, GeoapifyCafeMapper mapper) {
        this.webClientBuilder = webClientBuilder;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(geoapifyBaseUrl).build();
    }




    public List<Cafe> obtenerCafesMdp() {

        String resourcePath = "/v2/places";

        String categories = "catering.cafe";
        String filter = "place:5142a7026ec4ca4cc059149fe16f6a0243c0f00101f901e7eb330000000000c0020692030d4d61722064656c20506c617461";
        int limit = 30;

        GeoapifyResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(resourcePath)
                        .queryParam("categories", categories)
                        .queryParam("filter", filter)
                        .queryParam("limit", limit)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(GeoapifyResponse.class)
                .block();

        if (response == null || response.getFeatures() == null) {
            return new ArrayList<>();
        }

        return response.getFeatures().stream()
                .map(mapper::mapFeatureToCafe)
                .collect(Collectors.toList());

    }

}
