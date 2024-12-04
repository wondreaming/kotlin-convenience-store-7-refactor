package store.controller

import store.controller.adapter.ProductAdapter
import store.controller.adapter.PromotionAdapter
import store.view.InputView
import store.view.OutputView

class UserInterface(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView(),
    private val promotionAdatper: PromotionAdapter = PromotionAdapter(),
    private val productAdapter: ProductAdapter = ProductAdapter(),
) {
    fun handleWelcomeMsg(): String {
        outputView.showOutput("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n")
        val promotions = promotionAdatper.loadPromotionType()
        val products = productAdapter.loadProductType(promotions)
        for (product in products) {
            if (product.promotionProduct != null) {
                outputView.showOutput(
                    "- %s %d원 %d개 %s".format(
                        product.name,
                        product.promotionProduct.price,
                        product.promotionProduct.quantity,
                        product.promotionProduct.promotion.name
                    )
                )
            }

            if (product.nonPromotionProduct != null) {
                outputView.showOutput(
                    "- %s %d원 %d개".format(
                        product.name,
                        product.nonPromotionProduct.price,
                        product.nonPromotionProduct.quantity
                    )
                )
            }
        }
        outputView.showOutput("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])")
        val userInput = inputView.getInput()
        return userInput
    }
}