package com.lab.labbook.client;

import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.LogDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LogClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<LogDto> getLogs() {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.LOG_ENDPOINT).build().encode().toUri();
        LogDto[] logDto = restTemplate.getForObject(url, LogDto[].class);
        return Arrays.asList(Optional.ofNullable(logDto).orElse(new LogDto[0]));
    }
}
