package com.fkh.booklibrary.infrastructure.adapters.output.graphql

import arrow.core.right
import com.fkh.booklibrary.infrastructure.Configs
import com.fkh.booklibrary.infrastructure.adapters.output.graphql.model.FetchBookQuery
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import java.util.UUID
import org.apache.hc.core5.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BookAppGraphqlClientTest {

    private val bookAppServer: WireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort()).also { it.start() }

    private val bookAppGraphqlApi = Configs().let {
        it.bookAppGraphqlApi(
            jsonMapper = it.defaultObjectMapper(),
            baseUrl = bookAppServer.baseUrl(),
            connectionTimeOut = 3000,
            readTimeOut = 3000,
            callTimeOut = 3000
        )
    }

    private val bookAppGraphqlClient = BookAppGraphqlClient(bookAppGraphqlApi)

    @Test
    fun `should fetch book from BookApp graphql client`() {
        val bookId = BookId(UUID.randomUUID())
        val fetchBook = FetchBookQuery.FetchBook(
            id = bookId.value.toString(),
            name = "Introduction to Graphql",
            pageCount = 365,
            author = FetchBookQuery.Author (
                id = "01732888-e065-4b0a-b41c-59b586a131a1",
                firstName = "Faraz",
                lastName = "Khatami"
            )
        )
        bookAppServer.stubForBookAppFindBook(fetchBook)
        val bookExpected = with(fetchBook) {
            Book(
                id = BookId(UUID.fromString(id)),
                name = name,
                author = Author(
                    id = AuthorId(UUID.fromString(author.id)),
                    fullName = "${author.firstName} ${author.lastName}"
                )
            )
        }

        val result = bookAppGraphqlClient.find(bookId)

        assertThat(result).isEqualTo(bookExpected.right())
    }
}

private fun WireMockServer.stubForBookAppFindBook(
    book: FetchBookQuery.FetchBook,
) = this.stubFor(
    WireMock.post(WireMock.urlEqualTo("/graphql"))
        .willReturn(
            WireMock.status(HttpStatus.SC_OK)
                .withBody(
                    """
                    {
                        "data": {
                            "fetchBook": {
                                "id": "${book.id}",
                                "name": "${book.name}",
                                "pageCount": ${book.pageCount},
                                "author":  {
                                    "id": "${book.author.id}",
                                    "firstName": "${book.author.firstName}",
                                    "lastName": "${book.author.lastName}"
                                }
                            }
                        }
                    }
                    """
                )
        )
)
