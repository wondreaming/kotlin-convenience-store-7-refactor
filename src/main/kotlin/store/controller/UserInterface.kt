package store.controller

import store.view.InputView
import store.view.OutputView

class UserInterface(
    val inputView: InputView = InputView(),
    val outputView: OutputView = OutputView(),
) {
    fun handleWelcomeMsg() {
        outputView.showOutput("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n")

    }
}