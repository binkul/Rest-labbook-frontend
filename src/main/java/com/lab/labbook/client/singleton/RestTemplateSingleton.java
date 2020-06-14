package com.lab.labbook.client.singleton;

import lombok.Getter;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Getter
public class RestTemplateSingleton {
    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private RestTemplateSingleton() {}

    public static RestTemplateSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final RestTemplateSingleton INSTANCE = new RestTemplateSingleton();
    }
}
