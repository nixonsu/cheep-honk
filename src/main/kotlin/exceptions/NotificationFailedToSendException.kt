package exceptions

class NotificationFailedToSendException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
