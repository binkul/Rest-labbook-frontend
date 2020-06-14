package com.lab.labbook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.SupplierDto;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SupplierClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<SupplierDto> getAll(String nameFiler) {
        URI url = getUrlByName(nameFiler);
        SupplierDto[] suppliers = restTemplate.getForObject(url, SupplierDto[].class);
        return Arrays.asList(Optional.ofNullable(suppliers).orElse(new SupplierDto[0]));
    }

    public ResponseStatus add(SupplierDto supplierDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SUPPLIER_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.postForEntity(url, supplierDto, String.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus update(SupplierDto supplierDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SUPPLIER_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.put(url, supplierDto);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus delete(Long id) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.SUPPLIER_DELETE_ENDPOINT + id).build().encode().toUri();
        try {
            restTemplate.delete(url);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    private URI getUrlByName(String nameFilter) {
        if (nameFilter == null || nameFilter.isEmpty()) {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.SUPPLIER_ENDPOINT).build().encode().toUri();
        } else {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.SUPPLIER_NAME_ENDPOINT)
                    .queryParam("name", nameFilter)
                    .build().encode().toUri();
        }
    }
}
