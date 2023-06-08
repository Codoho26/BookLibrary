package com.fkh.booklibrary.application.service

import arrow.core.Either
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BookNotFound
import com.fkh.booklibrary.model.BorrowBook
import com.fkh.booklibrary.model.ports.BookFinder
import java.util.UUID

class BorrowBookService(
    private val bookFinder: BookFinder,
    private val requestId: ()->UUID
) {

    operator fun invoke(id: String): Either<BookNotFound, BorrowBook> = BookId(UUID.fromString(id))
        .let { bookFinder.find(it) }
        .map { BorrowBook.with(requestId(), it, true, "some location") }
}