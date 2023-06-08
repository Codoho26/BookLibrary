package com.fkh.booklibrary.infrastructure

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fkh.booklibrary.application.service.BorrowBookService
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.BookAppGraphqlApi
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.BookAppGraphqlClient
import com.fkh.booklibrary.model.ports.BookFinder
import java.util.UUID
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class Configs {

    @Bean
    fun bookFinder(
        bookAppGraphqlApi: BookAppGraphqlApi
    ): BookFinder = BookAppGraphqlClient(
        bookAppGraphqlApi = bookAppGraphqlApi
    )

    @Bean
    fun borrowBookService(
        bookFinder: BookFinder
    ) = BorrowBookService(
        bookFinder = bookFinder,
        requestId = { UUID.randomUUID() }
    )

    @Bean
    fun bookAppGraphqlClient(
        bookAppGraphqlApi: BookAppGraphqlApi
    ) = BookAppGraphqlClient(
        bookAppGraphqlApi = bookAppGraphqlApi
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

    @Bean
    fun bookAppGraphqlApi(
        jsonMapper: ObjectMapper,
        @Value("\${client.bookapp-api.base-url}") baseUrl: String,
        @Value("\${client.bookapp-api.connection-timeout}") connectionTimeOut: Long,
        @Value("\${client.bookapp-api.call-timeout}") callTimeOut: Long,
        @Value("\${client.bookapp-api.read-timeout}") readTimeOut: Long,
    ): BookAppGraphqlApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(jsonMapper))
            .client(
                OkHttpClient.Builder()
                    .callTimeout(callTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connectionTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .build()
            )
            .build()
            .create(BookAppGraphqlApi::class.java)
}
