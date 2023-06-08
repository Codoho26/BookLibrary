package com.fkh.booklibrary.model

import java.util.UUID

data class BorrowBook(
    val id: RequestId,
    val book: Book,
    val available: Boolean,
    val location: String,
) {

    companion object {

        fun with(requestId: UUID, book: Book, available: Boolean, location: String) = BorrowBook(
            id = RequestId(requestId),
            book = book,
            available = available,
            location = location
        )
    }
}

data class RequestId(val value: UUID)

data class Book(
    val id: BookId,
    val name: String,
    val author: Author,
)

data class BookId(val value: UUID)

data class Author(
    val id: AuthorId,
    val fullName: String?,
)

data class AuthorId(val value: UUID)