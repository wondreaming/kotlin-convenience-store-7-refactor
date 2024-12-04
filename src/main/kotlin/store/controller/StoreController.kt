package store.controller

class StoreController(
    private val userInterface: UserInterface = UserInterface(),
) {

    fun run() {
        val products = userInterface.handleWelcomeMsg()
    }
}