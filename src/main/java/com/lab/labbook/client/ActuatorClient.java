package com.lab.labbook.client;

import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.ActuatorDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public class ActuatorClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public ActuatorDto getInfo() {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.ACTUATOR_INFO_ENDPOINT).build().encode().toUri();
        ActuatorDto actuatorDto = restTemplate.getForObject(url, ActuatorDto.class);
        return Optional.ofNullable(actuatorDto).orElse(new ActuatorDto());
    }
}
