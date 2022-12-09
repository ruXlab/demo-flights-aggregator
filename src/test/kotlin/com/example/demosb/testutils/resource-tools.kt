package com.example.demosb.testutils

import com.example.demosb.DemoSbApplication

fun readResource(path: String): String =
    DemoSbApplication::class.java.classLoader.getResourceAsStream(path)
        .bufferedReader()
        .readText()
