package com.lab.labbook.client;

import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.WeatherDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WeatherClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<WeatherDto> getAll() {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.WEATHER_ENDPOINT).build().encode().toUri();
        WeatherDto[] weathers = restTemplate.getForObject(url, WeatherDto[].class);
        return Arrays.asList(Optional.ofNullable(weathers).orElse(new WeatherDto[0]));
    }
}
