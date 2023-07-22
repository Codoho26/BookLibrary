package com.fkh.booklibrary.model.ports

import arrow.core.Either
import com.fkh.booklibrary.model.Book
import com.fkh.booklibrary.model.BookId
import com.fkh.booklibrary.model.BookNotFound

interface BookFinder {

    suspend fun find(bookId: BookId): Either<BookNotFound, Book>
}