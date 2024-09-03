package exceptions

class TravelInfoNotFoundException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
