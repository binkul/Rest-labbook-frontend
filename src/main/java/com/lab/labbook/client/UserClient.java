package com.lab.labbook.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.UserDto;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<UserDto> getUsers(String nameFilter) {
        URI url = getUrlByName(nameFilter);
        UserDto[] users = restTemplate.getForObject(url, UserDto[].class);
        return Arrays.asList(Optional.ofNullable(users).orElse(new UserDto[0]));
    }

    public UserDto getByLogin(String login) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_LOGIN_ENDPOINT)
                .queryParam("login", login)
                .build().encode().toUri();
        UserDto userDto = restTemplate.getForObject(url, UserDto.class);
        return Optional.ofNullable(userDto).orElse(new UserDto());
    }

    public ResponseStatus add(UserDto userDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.postForEntity(url, userDto, String.class);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus update(UserDto userDto) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_SAVE_ENDPOINT).build().encode().toUri();
        try {
            restTemplate.put(url, userDto);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }

    public ResponseStatus delete(Long id) throws JsonProcessingException {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_DELETE_ENDPOINT + id).build().encode().toUri();
        try {
            restTemplate.delete(url);
            return new ResponseStatus(LocalDateTime.now().toString(), "200", "ok");
        } catch (HttpClientErrorException ex) {
            return new ObjectMapper().readValue(ex.getResponseBodyAsString(), ResponseStatus.class);
        }
    }


    private URI getUrlByName(String nameFilter) {
        if (nameFilter == null || nameFilter.isEmpty()) {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_ENDPOINT).build().encode().toUri();
        } else {
            return UriComponentsBuilder.fromHttpUrl(ClientConfig.USER_NAME_ENDPOINT)
                    .queryParam("name", nameFilter)
                    .build().encode().toUri();
        }
    }
}
