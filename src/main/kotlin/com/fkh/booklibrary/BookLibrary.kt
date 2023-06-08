package com.fkh.booklibrary

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookLibrary

fun main(args: Array<String>) {
    runApplication<BookLibrary>(*args)
}
