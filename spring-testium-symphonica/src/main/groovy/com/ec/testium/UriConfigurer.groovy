package com.ec.testium

import org.springframework.web.util.UriBuilder

@FunctionalInterface
interface UriConfigurer {

    void configure(UriBuilder uriBuilder)
}
