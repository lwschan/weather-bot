package dev.lewischan.weatherbot.core.extension

import java.util.*

fun <T> Optional<T>.unwrap(): T? = orElse(null)
