package store.controller

import store.controller.adapter.ProductAdapter
import store.controller.adapter.PromotionAdapter
import store.controller.domain.UserInteractionController

class StoreController(
    private val promotionAdapter: PromotionAdapter = PromotionAdapter(),
    private val productAdapter: ProductAdapter = ProductAdapter(),
    private val userInteractionController: UserInteractionController = UserInteractionController(),
) {
    fun run() {
        val promotions = promotionAdapter.loadPromotion()
        val products = productAdapter.run(promotions)
        val userBuying = userInteractionController.handleWelcomeMsg(products)
    }
}