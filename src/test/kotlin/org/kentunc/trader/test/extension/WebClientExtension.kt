package org.kentunc.trader.test.extension

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class WebClientExtension : BeforeAllCallback, AfterAllCallback {

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }

    private lateinit var mockServer: MockWebServer

    override fun beforeAll(context: ExtensionContext?) {
        mockServer = MockWebServer()
        mockServer.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        mockServer.shutdown()
    }

    fun createWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:${mockServer.port}")
            .build()
    }

    fun enqueueResponse(body: String?, status: HttpStatus = HttpStatus.OK, headers: HttpHeaders = HttpHeaders.EMPTY) {
        val response = MockResponse().apply {
            body?.also {
                setBody(body)
            }
            setResponseCode(status.value())

            headers.forEach { key, value -> addHeader(key, value) }
            addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }
        mockServer.enqueue(response)
    }

    fun assertRequest(method: HttpMethod, path: String, body: Any? = null) {
        val recordedRequest = mockServer.takeRequest()
        assertAll(
            Executable { assertEquals(method.name, recordedRequest.method) },
            Executable { assertEquals(path, recordedRequest.path) },
            Executable {
                body?.also {
                    val expected = recordedRequest.body.readUtf8()
                    val actual = objectMapper.writeValueAsString(body)
                    assertEquals(expected, actual)
                }
            }
        )
    }
}
