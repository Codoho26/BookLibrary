package com.fkh.booklibrary.infrastructure.adapters.output.graphql

data class GraphqlCallNonSuccessfulError(
    val graphqlClient: String,
    val errorBody: String?
) : RuntimeException("Graphql call with '$graphqlClient' failed with body '$errorBody' ")