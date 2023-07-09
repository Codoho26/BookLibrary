package com.fkh.booklibrary.application.service

import arrow.core.right
import com.fkh.booklibrary.model.Author
import com.fkh.booklibrary.model.AuthorId
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BorrowBook
import com.fkh.booklibrary.model.RequestId
import com.fkh.booklibrary.model.ports.BookFinder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BorrowBookServiceTest {

    private val bookFinder = mockk<BookFinder>()

    private val requestId = UUID.randomUUID()
    private val borrowBookService = BorrowBookService(bookFinder) { requestId }

    @Test
    fun `should borrow book`() {
        val bookId = "f0249d34-945c-4b8d-9e68-6bafb6fae7e0"
        val book = Book(
            id = BookId(UUID.fromString(bookId)),
            name = "Introduction to Graphql",
            author = Author(
                id = AuthorId(UUID.fromString("01732888-e065-4b0a-b41c-59b586a131a1")),
                fullName = "Faraz Khatami",
            )
        )
        val borrowBook = BorrowBook(
            id = RequestId(requestId),
            book = book,
            available = true,
            location = "some location"
        )
        coEvery {  bookFinder.find(BookId(UUID.fromString(bookId))) } returns book.right()

        val result = borrowBookService(bookId)

        assertThat(result).isEqualTo(borrowBook.right())
    }
}
