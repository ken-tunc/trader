package org.kentunc.trader.test.util

object ResourceReader {

    fun readResource(path: String): String {
        return javaClass.getResource(path).readText()
    }
}
