package ru.ithex.baseweb.service.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ru.ithex.baseweb.exception.RestException;
import ru.ithex.baseweb.exception.RestServiceSystemException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

@Service
public class RestService {
    private final RestTemplate restTemplate;

    private Runnable handleErrorAction;

    public RestService() {
        restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setOutputStreaming(false);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public boolean hasError(ClientHttpResponse response) {
                HttpStatus.Series series = null;
                try {
                    series = response.getStatusCode().series();
                } catch (IOException e){
                    throw new RestServiceSystemException("Ошибка извлечения статуса ответа", e);
                }
                return series != HttpStatus.Series.SUCCESSFUL;
            }

            @Override
            public void handleError(URI uri, HttpMethod httpMethod, ClientHttpResponse response) {
                if (handleErrorAction != null)
                    handleErrorAction.run();
                throw new RestException(traceResponse(response));
            }
        });
    }

    public <T> ResponseEntity<T> exchange(final String url, final HttpMethod httpMethod, final HttpEntity request, final Class<T> responseType, final Runnable handleErrorAction){
        this.handleErrorAction = handleErrorAction;
        return restTemplate.exchange(url, httpMethod, request, responseType);
    }

    private String traceResponse(ClientHttpResponse response) {
        String s = null;
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(response.getBody()));
            s =bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        }catch (IOException e){
            throw new RestServiceSystemException("Ошибка чтения тела ответа", e);
        }finally {
            if (bufferedReader != null)
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    throw new RestServiceSystemException("System error", e);
                }
        }
        return s;
    }
}
