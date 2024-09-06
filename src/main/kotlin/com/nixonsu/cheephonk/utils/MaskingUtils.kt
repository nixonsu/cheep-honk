package com.nixonsu.cheephonk.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

private val sensitiveQueryParamPattern: Pattern = Pattern.compile("(api_key|apiKey|key|token|password)=([^&]+)")

fun maskSensitiveData(logMessage: String): String {
    return sensitiveQueryParamPattern.matcher(logMessage).replaceAll("$1=[REDACTED]")
}

class MaskingLogger(private val logger: Logger) {
    fun info(message: String) {
        logger.info(maskSensitiveData(message))
    }

    fun debug(message: String) {
        logger.debug(maskSensitiveData(message))
    }

    fun error(message: String) {
        logger.error(maskSensitiveData(message))
    }

    fun warn(message: String) {
        logger.warn(maskSensitiveData(message))
    }

    companion object {
        fun getLogger(clazz: Class<*>): MaskingLogger {
            return MaskingLogger(LoggerFactory.getLogger(clazz))
        }
    }
}
