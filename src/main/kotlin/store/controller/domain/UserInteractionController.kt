package store.controller.domain

import store.model.Product
import store.view.InputView
import store.view.OutputView
import store.util.DecimalUtil.getDecimalFormat

class UserInteractionController(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView(),
) {

    fun handleWelcomeMsg(products: List<Product>): String {
        outputView.showOutput("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n")
        for (product in products) {
            val price = getDecimalFormat(product.price)
            val quantity = if (product.quantity == 0) "재고 없음" else product.quantity.toString() + "개"
            outputView.showOutput(
                "- %s %s원 %s %s".format(
                    product.name,
                    price,
                    quantity,
                    product.promotion?.name ?: ""
                )
            )
        }
        outputView.showOutput("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])")
        return inputView.getInput()
    }
}