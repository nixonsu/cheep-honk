package exceptions

class AuthenticationFailedException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
