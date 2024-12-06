package store.util

fun <T> retryWhenNoException(action: () -> T): T {
    while (true) {
        try {
            return action.invoke()
        } catch (e: IllegalArgumentException) {
            println(e.message + "\n")
        }
    }
}