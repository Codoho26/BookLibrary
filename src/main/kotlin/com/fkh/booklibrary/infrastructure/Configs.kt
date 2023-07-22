package com.fkh.booklibrary.infrastructure

import com.apollographql.apollo3.ApolloClient
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fkh.booklibrary.application.service.BorrowBookService
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.BookAppGraphqlClient
import com.fkh.booklibrary.model.ports.BookFinder
import java.util.UUID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class Configs {

    @Bean
    fun borrowBookService(
        bookFinder: BookFinder
    ) = BorrowBookService(
        bookFinder = bookFinder,
        requestId = { UUID.randomUUID() }
    )

    @Bean
    fun bookAppGraphqlClient(
    ) = BookAppGraphqlClient(
        bookAppApolloClient = ApolloClient.Builder()
            .serverUrl("http://localhost:8080/graphql")
            .build()
    )

    @Bean
    @Primary
    fun defaultObjectMapper(): ObjectMapper = jacksonObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .registerModules(JavaTimeModule())
        .findAndRegisterModules()

}
