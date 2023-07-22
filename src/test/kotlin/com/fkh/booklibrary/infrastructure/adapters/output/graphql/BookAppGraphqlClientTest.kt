package com.fkh.booklibrary.infrastructure.adapters.output.graphql

import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.fkh.booklibrary.infrastructure.Configs
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import kotlinx.coroutines.runBlocking
import java.util.UUID
import org.apache.hc.core5.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BookAppGraphqlClientTest {
    private val bookId = BookId(UUID.randomUUID())

    private val fetchBook = FetchBookQuery.FetchBook(
        id = bookId.value.toString(),
        name = "Introduction to Graphql",
        pageCount = 365,
        author = FetchBookQuery.Author(
            id = "01732888-e065-4b0a-b41c-59b586a131a1",
            firstName = "Faraz",
            lastName = "Khatami"
        )
    )

    private val bookExpected = with(fetchBook) {
        Book(
            id = BookId(UUID.fromString(id)),
            name = name,
            author = Author(
                id = AuthorId(UUID.fromString(author.id)),
                fullName = "${author.firstName} ${author.lastName}"
            )
        )
    }

    @OptIn(ApolloExperimental::class)
    private val apolloClient = ApolloClient.Builder()
        .networkTransport(QueueTestNetworkTransport())
        .build()
        .apply {
            enqueueTestResponse(
                FetchBookQuery(bookId.value.toString()), FetchBookQuery.Data(fetchBook = fetchBook)
            )
        }

    private val bookAppGraphqlClient = BookAppGraphqlClient(apolloClient)

    @Test
    fun `should fetch book from BookApp graphql client`() {
        val result = runBlocking {
            bookAppGraphqlClient.find(bookId)
        }

        assertThat(result).isEqualTo(bookExpected.right())
    }
}
