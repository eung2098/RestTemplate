package com.example.client.service;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateService {

    // http://localhost/api/server/hello 호출 ->
    // response 요청

    public UserResponse hello(){

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("api/server/hello")
                .queryParam("name", "abcd")
                .queryParam("age", "200")
                .encode()
                .build()
                .toUri();

        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> result = restTemplate.getForEntity(uri, UserResponse.class);

        System.out.println(result.getStatusCode());
        System.out.println(result.getBody());
        return result.getBody();

    }

    // http://localhost:9090/api/server/user/{userId}/name/{userName}
    public UserResponse post(){

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"hong")
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req= new UserRequest();
        req.setName("hong");
        req.setAge(20);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

        return response.getBody();
    }

    public UserResponse exchange(){

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"hong")
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mapper -> json -> rest template -> http body json
        UserRequest req= new UserRequest();
        req.setName("hong");
        req.setAge(20);

        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-authorization", "abcd")
                .header("custom-header", "12345")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);

        return response.getBody();
    }

    public Req<UserResponse> genericExchange(){

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100,"hong")
                .toUri();
        System.out.println(uri);

        // http body -> object -> object mapper -> json -> rest template -> http body json

        UserRequest userRequest= new UserRequest();
        userRequest.setName("hong");
        userRequest.setAge(20);

        Req<UserRequest> req = new Req<>();
        req.setHeader(
                new Req.Header()
        );

        req.setResBody(
                userRequest
        );


        RequestEntity<Req<UserRequest>> requestEntity = RequestEntity
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-authorization", "abcd")
                .header("custom-header", "12345")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();



        ResponseEntity<Req<UserResponse>> response =
                restTemplate.exchange(requestEntity,
                        new ParameterizedTypeReference<>(){});

        return response.getBody();
    }
}
