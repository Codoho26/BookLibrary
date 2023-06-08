package com.fkh.booklibrary.infrastructure.adapters.output.graphql

data class GraphqlCallNonSuccessfulError(
    val graphqlClient: String,
    val errorBody: String?,
    val status: Int
) : RuntimeException("Graphql call with '$graphqlClient' failed with status '$status' and body '$errorBody' ")