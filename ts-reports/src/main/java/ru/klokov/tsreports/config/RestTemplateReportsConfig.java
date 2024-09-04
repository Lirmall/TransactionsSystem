package ru.klokov.tsreports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateReportsConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(new RetryInterceptor());

        return restTemplate;
    }

    private static class RetryInterceptor implements ClientHttpRequestInterceptor {

        private static final int MAX_ATTEMPTS = 3; // Максимальное количество попыток
        private static final long BACKOFF_MILLIS = 1000; // Интервал между попытками в миллисекундах

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            int attempt = 1;
            while (attempt <= MAX_ATTEMPTS) {
                try {
                    return execution.execute(request, body);
                } catch (ResourceAccessException e) {
                    if (attempt == MAX_ATTEMPTS) {
                        throw e;
                    }
                    attempt++;
                    try {
                        Thread.sleep(BACKOFF_MILLIS * (long) Math.pow(2, attempt - 1)); // Экспоненциальное увеличение задержки
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                }
            }
            throw new ResourceAccessException("All attempts failed");
        }
    }
}
