package com.ec.testium

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.util.UriBuilder

import java.util.function.Consumer
import java.util.function.Function

@RequiredArgsConstructor
abstract class BaseTestClient {

    protected final WebTestClient testClient

    protected abstract String basePath()

    protected WebTestClient.ResponseSpec doRequest(HttpMethod method,
                                                   UriConfigurer uriConfigurer = null,
                                                   Consumer<HttpHeaders> headers = null,
                                                   Object body = null) {
        def requestSpec = testClient.method(method)
                .uri(buildUriFunction(uriConfigurer))
                .headers(buildHeaders(headers))
                .accept(MediaType.APPLICATION_JSON)

        if (body) {
            requestSpec.body(BodyInserters.fromValue(body))
        }

        requestSpec.exchange()
    }

    protected WebTestClient.ResponseSpec doGet(
            UriConfigurer uriConfigurer = null,
            Consumer<HttpHeaders> headers = null) {
        doRequest(HttpMethod.GET, uriConfigurer, headers)
    }

    protected WebTestClient.ResponseSpec doPost(
            Object body,
            UriConfigurer uriConfigurer = null,
            Consumer<HttpHeaders> headers = null) {
        doRequest(HttpMethod.POST, uriConfigurer, headers, body)
    }

    protected WebTestClient.ResponseSpec doPut(
            Object body,
            UriConfigurer uriConfigurer,
            Consumer<HttpHeaders> headers = null) {
        doRequest(HttpMethod.PUT, uriConfigurer, headers, body)
    }

    protected WebTestClient.ResponseSpec doDelete(
            UriConfigurer uriConfigurer,
            Consumer<HttpHeaders> headers = null) {
        doRequest(HttpMethod.DELETE, uriConfigurer, headers)
    }

    private Function<UriBuilder, URI> buildUriFunction(
            UriConfigurer builder) {
        { uriBuilder ->
            def uriComponentsBuilder = uriBuilder.path(basePath())
            builder?.configure(uriComponentsBuilder)
            uriComponentsBuilder.build()
        }
    }

    private static Consumer<HttpHeaders> buildHeaders(
            Consumer<HttpHeaders> customHeaders) {
        { httpHeaders ->
            customHeaders?.accept(httpHeaders)
        }
    }
}
