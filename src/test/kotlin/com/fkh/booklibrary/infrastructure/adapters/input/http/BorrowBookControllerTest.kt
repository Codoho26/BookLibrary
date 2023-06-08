package com.fkh.booklibrary.infrastructure.adapters.input.http

import arrow.core.right
import com.fkh.booklibrary.application.service.BorrowBookService
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BorrowBook
import com.fkh.booklibrary.model.RequestId
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [BorrowBookController::class])
@ExtendWith(MockKExtension::class)
class BorrowBookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var borrowBookService: BorrowBookService

    @Test
    fun `should borrow book`() {
        val bookId = "f0249d34-945c-4b8d-9e68-6bafb6fae7e0"
        val borrowBook = BorrowBook(
            id = RequestId(UUID.randomUUID()),
            book = Book(
                id = BookId(UUID.fromString(bookId)),
                name = "Introduction to Graphql",
                author = Author(
                    id = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1")),
                    fullName = "Faraz Khatami",
                )
            ),
            available = true,
            location = "West avenue library"
        )
        every { borrowBookService(bookId) } returns borrowBook.right()

        val result = mockMvc.get("/library/books/borrow/book?id=$bookId")

        result
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(
                        with(borrowBook) {
                            """
                            {
                                "id": {
                                    "value": "${id.value}"
                                },
                                "book": {
                                    "id": {
                                        "value": "${book.id.value}"
                                    },
                                    "name": "${book.name}",
                                    "author": {
                                        "id": {
                                            "value": "${book.author.id.value}"
                                        },
                                        "fullName": "${book.author.fullName}"
                                    }
                                },
                                "available": true,
                                "location": "West avenue library"
                            }
                            """
                        }
                    )
                }

            }
    }
}
