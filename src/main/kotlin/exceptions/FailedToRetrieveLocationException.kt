package exceptions

class FailedToRetrieveLocationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
