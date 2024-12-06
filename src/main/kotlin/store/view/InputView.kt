package store.view

import camp.nextstep.edu.missionutils.Console

class InputView {
    fun getInput(): String {
        val userInput = Console.readLine().trim()
        return userInput
    }
}