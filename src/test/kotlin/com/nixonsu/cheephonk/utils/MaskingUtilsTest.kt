package com.nixonsu.cheephonk.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MaskingUtilsTest {
    @Nested
    inner class MaskSensitiveData {
        @Test
        fun `should mask sensitive data in info logs`() {
            val sensitiveMessage = "Requesting URL: https://example.com/resource?api_key=abcd1234&token=xyz123&key=123123"
            val expectedMaskedMessage = "Requesting URL: https://example.com/resource?api_key=[REDACTED]&token=[REDACTED]&key=[REDACTED]"

            val result = maskSensitiveData(sensitiveMessage)

            assertEquals(expectedMaskedMessage, result)
        }

        @Test
        fun `should not modify logs with no sensitive data`() {
            val message = "Requesting URL: https://example.com/resource?queryParam=value"

            val result = maskSensitiveData(message)

            assertEquals(message, result)
        }
    }
}
