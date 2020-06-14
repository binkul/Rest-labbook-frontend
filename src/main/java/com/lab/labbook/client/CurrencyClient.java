package com.lab.labbook.client;

import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.CurrencyDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CurrencyClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<CurrencyDto> getAll() {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.CURRENCY_ENDPOINT).build().encode().toUri();
        CurrencyDto[] currencyDto = restTemplate.getForObject(url, CurrencyDto[].class);
        return Arrays.asList(Optional.ofNullable(currencyDto).orElse(new CurrencyDto[0]));
    }
}
