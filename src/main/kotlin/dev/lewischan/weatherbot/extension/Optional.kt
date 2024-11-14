package dev.lewischan.weatherbot.extension

import java.util.*

fun <T> Optional<T>.unwrap(): T? = orElse(null)
