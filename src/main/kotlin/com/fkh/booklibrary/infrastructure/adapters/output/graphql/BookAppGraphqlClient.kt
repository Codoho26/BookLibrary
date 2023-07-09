package com.fkh.booklibrary.infrastructure.adapters.output.graphql

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery.FetchBook
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BookNotFound
import com.fkh.booklibrary.model.ports.BookFinder
import java.util.UUID

data class BookAppGraphqlClient(
    private val bookAppApolloClient: ApolloClient,
): BookFinder {

    override suspend fun find(bookId: BookId): Either<BookNotFound, Book> =
        bookAppApolloClient.query(FetchBookQuery(bookId.value.toString()))
            .execute()
            .let(::extractResponse)
            .map { it.toDomain() }


    private fun extractResponse(response: ApolloResponse<FetchBookQuery.Data>): Either<BookNotFound, FetchBook> = when {
        response.hasErrors() -> throw GraphqlCallNonSuccessfulError(
            graphqlClient = BookAppGraphqlClient::class.simpleName!!,
            errorBody = response.errors.toString()
        )
        else -> response.data?.fetchBook?.right() ?: BookNotFound.left()
    }

    private fun FetchBook.toDomain() = Book(
        id = BookId(UUID.fromString(id)),
        name = name,
        author = Author(
            id = AuthorId(UUID.fromString(author.id)),
            fullName = "${author.firstName} ${author.lastName}"
        )
    )
}