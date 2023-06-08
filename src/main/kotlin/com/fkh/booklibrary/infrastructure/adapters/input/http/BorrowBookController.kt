package com.fkh.booklibrary.infrastructure.adapters.input.http

import com.fkh.booklibrary.application.service.BorrowBookService
import com.fkh.booklibrary.model.BorrowBook
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/library/books"])
class BorrowBookController(
    private val borrowBookService: BorrowBookService
) {

    @GetMapping(path = ["/borrow/book"])
    fun borrow(
        @RequestParam id: String,
    ): ResponseEntity<BorrowBook> = borrowBookService(id).fold(
        ifLeft = {throw Exception()}, ifRight = { ResponseEntity.status(HttpStatus.OK).body(it) }
    )
}