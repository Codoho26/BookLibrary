package com.fkh.booklibrary.model

sealed interface DomainError

object BookNotFound: DomainError