package com.example.demosb.errors

class ApiException(
    val code: Int,
    val error: String,
    val humanError: String,
    throwable: Throwable? = null
): Exception(error, throwable) {
}