package com.lab.labbook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.SeriesDto;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SeriesClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<SeriesDto> getSeries(String titleFilter) {
        URI url = getUrlByName(titleFilter);
        SeriesDto[] series = restTemplate.getForObject(url, SeriesDto[].class);
        return Arrays.asList(Optional.ofNullable(series).orElse(new SeriesDto[0]));
    }

    public ResponseStatus add(SeriesDto seriesDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SERIES_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.postForEntity(url, seriesDto, String.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus update(SeriesDto seriesDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SERIES_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.put(url, seriesDto);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus delete(Long id) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SERIES_DELETE_ENDPOINT + id).build().encode().toUri();
        try {
            restTemplate.delete(url);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    private URI getUrlByName(String titleFilter) {
        if (titleFilter == null || titleFilter.isEmpty()) {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.SERIES_ENDPOINT).build().encode().toUri();
        } else {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.SERIES_TITLE_ENDPOINT)
                    .queryParam("title", titleFilter)
                    .build().encode().toUri();
        }
    }
}
