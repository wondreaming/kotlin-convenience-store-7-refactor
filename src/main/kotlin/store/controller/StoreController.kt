package store.controller

import store.controller.adapter.ProductAdapter
import store.controller.adapter.PromotionAdapter
import store.controller.domain.UserInteractionController
import store.controller.validator.BuyingValidator
import store.model.Product
import store.util.retryWhenNoException

class StoreController(
    private val promotionAdapter: PromotionAdapter = PromotionAdapter(),
    private val productAdapter: ProductAdapter = ProductAdapter(),
    private val userInteractionController: UserInteractionController = UserInteractionController(),
    private val buyingValidator: BuyingValidator = BuyingValidator(),
) {
    fun run() {
        val promotions = promotionAdapter.loadPromotion()
        val storageProducts = productAdapter.run(promotions)
        val userBuying = getUserBuying(storageProducts)
    }

    private fun getUserBuying(storageProducts: List<Product>): String {
        val userBuying = retryWhenNoException {
            val userBuying = userInteractionController.handleWelcomeMsg(storageProducts)
            buyingValidator(userBuying, storageProducts)
            userBuying
        }
        return userBuying
    }
}