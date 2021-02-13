package org.kentunc.trader.extension

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun <T> Collection<T>.forEachParallel(f: suspend (T) -> Unit): Unit = runBlocking {
    map { async { f(it) } }.forEach { it.await() }
}
