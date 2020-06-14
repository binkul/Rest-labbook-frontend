package com.lab.labbook.client;

import com.lab.labbook.client.singleton.RestTemplateSingleton;
import com.lab.labbook.config.ClientConfig;
import com.lab.labbook.domain.IngredientDto;
import com.lab.labbook.domain.IngredientMoveDto;
import com.lab.labbook.domain.PriceDto;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IngredientClient {
    private final RestTemplate restTemplate = RestTemplateSingleton.getInstance().getRestTemplate();

    public List<IngredientDto> getAll(Long labId) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_ENDPOINT + labId).build().encode().toUri();;
        IngredientDto[] recipe = restTemplate.getForObject(url, IngredientDto[].class);
        return Arrays.asList(Optional.ofNullable(recipe).orElse(new IngredientDto[0]));
    }

    public PriceDto getPrice(Long labId) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_PRICE_ENDPOINT + labId).build().encode().toUri();;
        PriceDto price = restTemplate.getForObject(url, PriceDto.class);
        return Optional.ofNullable(price).orElseGet(() -> new PriceDto(BigDecimal.ZERO, BigDecimal.ZERO));
    }

    public BigDecimal getVoc(Long labId) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_VOC_ENDPOINT + labId).build().encode().toUri();;
        BigDecimal voc = restTemplate.getForObject(url, BigDecimal.class);
        return voc != null ? voc : BigDecimal.ZERO;
    }

    public BigDecimal getSum(Long labId) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_SUM_ENDPOINT + labId).build().encode().toUri();;
        BigDecimal sum = restTemplate.getForObject(url, BigDecimal.class);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    public void move(IngredientMoveDto moveDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_MOVE_ENDPOINT).build().encode().toUri();
        restTemplate.put(url, moveDto);
    }

    public void add(IngredientDto ingredientDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_SAVE_ENDPOINT).build().encode().toUri();
        restTemplate.postForLocation(url, ingredientDto);
    }

    public void update(IngredientDto ingredientDto) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_SAVE_ENDPOINT).build().encode().toUri();
        restTemplate.put(url, ingredientDto);
    }

    public void delete(Long id) {
        URI url = UriComponentsBuilder.fromHttpUrl(ClientConfig.INGR_DELETE_ENDPOINT + id).build().encode().toUri();
        restTemplate.delete(url);
    }
}
