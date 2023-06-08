package com.fkh.booklibrary.infrastructure.adapters.output.graphql

import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery.FetchBook
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface BookAppGraphqlApi {

    @POST("/graphql")
    fun fetchBook(
        @Body body: FetchBookBody
    ): Call<FetchBookResponse>
}

data class FetchBookResponse(
    val data: FetchBookDto
)

data class FetchBookDto(
    val fetchBook: FetchBook
)

data class FetchBookBody(
    val operationName: String,
    val variables: FetchBookVariables,
    val query: String
)

data class FetchBookVariables(
    val id: String
)
