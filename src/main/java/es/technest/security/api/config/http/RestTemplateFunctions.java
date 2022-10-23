package es.technest.security.api.config.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@UtilityClass
public class RestTemplateFunctions {

    public static ResponseEntity<String> putForStringEntity(RestTemplate restTemplate, String url, @Nullable Object request, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, PUT, new HttpEntity<>(request), String.class, uriVariables);
    }

    public static ResponseEntity<String> putForStringWithEmptyBody(RestTemplate restTemplate, String url, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, PUT, new HttpEntity<>("{}"), String.class, uriVariables);
    }

    public static ResponseEntity<String> getForStringEntityWithHeaders(RestTemplate restTemplate, String url, HttpHeaders headers, Object... uriVariables) {
        return restTemplate.exchange(url, GET, new HttpEntity<>(headers), String.class, uriVariables);
    }

    public static ResponseEntity<String> getForStringEntity(RestTemplate restTemplate, String url, Object... uriVariables) throws RestClientException {
        return restTemplate.getForEntity(url, String.class, uriVariables);
    }

    public static ResponseEntity<String> postForStringWithEmptyBody(RestTemplate restTemplate, String url, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, POST, new HttpEntity<>("{}"), String.class, uriVariables);
    }

    public static ResponseEntity<String> postForStringEntity(RestTemplate restTemplate, String url, @Nullable Object request, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, POST, new HttpEntity<>(request), String.class, uriVariables);
    }

    public static ResponseEntity<String> postForStringEntity(RestTemplate restTemplate, String url, @Nullable Object request, HttpHeaders headers, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, POST, new HttpEntity<>(request, headers), String.class, uriVariables);
    }

    public static <T> T responseToClassOrThrow(ObjectMapper objectMapper, ResponseEntity<String> responseEntity, Class<T> clazz) {
        return Try.of(() -> objectMapper.readValue(responseEntity.getBody(), clazz)).getOrElseThrow((throwable -> new RuntimeJsonMappingException(throwable.getMessage())));
    }

    public static <T> T responseToClassOrThrow(ObjectMapper objectMapper, ResponseEntity<String> responseEntity, TypeReference valueTypeRef) {
        return (T) Try.of(() -> objectMapper.readValue(responseEntity.getBody(), valueTypeRef)).getOrElseThrow((throwable -> new RuntimeJsonMappingException(throwable.getMessage())));
    }

    public static String urlWithQueryParam(String baseUrl, MultiValueMap<String, String> uriVariables) {
        return fromHttpUrl(baseUrl).queryParams(uriVariables).toUriString();
    }

    public static String mapToQueryParamString(MultiValueMap<String, String> uriVariables) {
        List<NameValuePair> list = uriVariables.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(singleVale -> new BasicNameValuePair(entry.getKey(), singleVale)))
                .collect(toList());
        return Try.of(() -> new URIBuilder().setParameters(list).build().toString()).getOrElse((String) null);

    }

    public static <T> List<Page<T>> requestAllPages(Function<PageRequest, Page<T>> requestPageFunction, Sort sort) {
        int pageSize = 100;
        int pageToQuery = 0;
        val allContent = new ArrayList<Page<T>>();
        Page<T> pageResponse;
        do {
            pageResponse = requestPageFunction.apply(PageRequest.of(pageToQuery, pageSize, sort));
            allContent.add(pageResponse);
            pageToQuery++;
        } while (pageToQuery < pageResponse.getTotalPages());

        return allContent;
    }

}
