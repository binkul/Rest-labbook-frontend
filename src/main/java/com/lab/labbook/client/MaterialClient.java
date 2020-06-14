package com.lab.labbook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.GhsDto;
import com.lab.labbook.domain.LabBookDto;
import com.lab.labbook.domain.MaterialDto;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MaterialClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<MaterialDto> getAll(String nameFiler) {
        URI url = getUrlByName(nameFiler);
        MaterialDto[] materials = restTemplate.getForObject(url, MaterialDto[].class);
        return Arrays.asList(Optional.ofNullable(materials).orElse(new MaterialDto[0]));
    }

    public MaterialDto getMaterial(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_SLASH_ENDPOINT + id).build().encode().toUri();
        MaterialDto material = restTemplate.getForObject(url, MaterialDto.class);
        return Optional.ofNullable(material).orElse(new MaterialDto());
    }

    public ResponseStatus add(MaterialDto materialDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.postForEntity(url, materialDto, String.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus update(MaterialDto materialDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.put(url, materialDto);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus updateClp(Long id, GhsDto ghsDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_SLASH_ENDPOINT + id).build().encode().toUri();
        try {
            restTemplate.patchForObject(url, ghsDto, LabBookDto.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus delete(Long id) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_SLASH_ENDPOINT + id).build().encode().toUri();
        try {
            restTemplate.delete(url);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    private URI getUrlByName(String nameFilter) {
        if (nameFilter == null || nameFilter.isEmpty()) {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_ENDPOINT).build().encode().toUri();
        } else {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.MAT_NAME_ENDPOINT)
                    .queryParam("name", nameFilter)
                    .build().encode().toUri();
        }
    }
}
