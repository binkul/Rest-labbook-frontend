package com.lab.labbook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.LabBookDto;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LabBookClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public LabBookDto getById(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.LAB_SLASH_ENDPOINT + id).build().encode().toUri();
        LabBookDto labBookDto = restTemplate.getForObject(url, LabBookDto.class);
        return Optional.ofNullable(labBookDto).orElseGet(LabBookDto::new);
    }

    public List<LabBookDto> getByUser(Long userId, String titleFilter) {
        URI url = getUrlByUserAndTitle(userId, titleFilter);
        LabBookDto[] labBooks = restTemplate.getForObject(url, LabBookDto[].class);
        return Arrays.asList(Optional.ofNullable(labBooks).orElse(new LabBookDto[0]));
    }

    public ResponseStatus add(LabBookDto labBookDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.LAB_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.postForEntity(url, labBookDto, String.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus update(Map<String, String> updates, Long labId) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.LAB_SLASH_ENDPOINT + labId).build().encode().toUri();
        try {
            restTemplate.patchForObject(url, updates, LabBookDto.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus delete(Long userId) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.LAB_SLASH_ENDPOINT + userId).build().encode().toUri();
        try {
            restTemplate.delete(url);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }

    }

    private URI getUrlByUserAndTitle(Long userId, String titleFilter) {
        return UriComponentsBuilder.fromHttpUrl(ClientConfig.LAB_USER_ENDPOINT + userId + "/title")
                .queryParam("title", titleFilter)
                .build().encode().toUri();
    }
}
