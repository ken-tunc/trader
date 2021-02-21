package org.kentunc.trader.infrastructure.repository.webclient.http.connector

import org.springframework.http.client.reactive.ClientHttpRequest
import java.time.ZonedDateTime
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class BitflyerSigner(private val accessKey: String, private val secretKey: String) {

    companion object {
        private const val HASH_ALGORITHM = "HmacSHA256"
        private const val HEADER_ACCESS_KEY = "ACCESS-KEY"
        private const val HEADER_ACCESS_TIMESTAMP = "ACCESS-TIMESTAMP"
        private const val HEADER_ACCESS_SIGN = "ACCESS-SIGN"
    }

    fun injectHeader(clientRequest: ClientHttpRequest, body: ByteArray?) {
        val timestamp = ZonedDateTime.now().toEpochSecond().toString()
        val text = timestamp + clientRequest.method.toString() + clientRequest.uri.path + (body?.toString() ?: "")

        clientRequest.headers.apply {
            add(HEADER_ACCESS_KEY, accessKey)
            add(HEADER_ACCESS_TIMESTAMP, timestamp)
            add(HEADER_ACCESS_SIGN, computeHash(text))
        }
    }

    private fun computeHash(text: String): String {
        val mac = Mac.getInstance(HASH_ALGORITHM).apply {
            init(SecretKeySpec(secretKey.toByteArray(), HASH_ALGORITHM))
        }
        return mac.doFinal(text.toByteArray())
            .joinToString("") { String.format("%02x", it and 255.toByte()) }
    }
}
