package com.fkh.booklibrary.infrastructure.adapters.output.graphql

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery.FetchBook
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BookNotFound
import com.fkh.booklibrary.model.ports.BookFinder
import java.util.UUID
import org.springframework.http.HttpStatus.NOT_FOUND
import retrofit2.Response

data class BookAppGraphqlClient(
    private val bookAppGraphqlApi: BookAppGraphqlApi,
): BookFinder {

    override fun find(bookId: BookId): Either<BookNotFound, Book> = bookAppGraphqlApi.fetchBook(
//        """
//            {
//                "query": "${FetchBookQuery.OPERATION_DOCUMENT}",
//                "variables": { "id": "${bookId.value}" }
//            }
//            """
        body = FetchBookBody(
            operationName = FetchBookQuery.OPERATION_NAME,
            variables = FetchBookVariables(id = bookId.value.toString()),
            query = FetchBookQuery.OPERATION_DOCUMENT
        )
    ).execute()
        .let(::extractResponse)
        .map { it.toDomain() }

    private fun extractResponse(response: Response<FetchBookResponse>): Either<BookNotFound, FetchBook> = when {
        response.isSuccessful -> response.body()?.data?.fetchBook?.right() ?: BookNotFound.left()
        response.code() == NOT_FOUND.value() -> BookNotFound.left()
        else -> throw GraphqlCallNonSuccessfulError(
            graphqlClient = BookAppGraphqlClient::class.simpleName!!,
            errorBody = response.errorBody()?.charStream()?.readText()?.trimIndent(),
            status = response.code()
        )
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