package store.controller

import store.controller.adapter.ProductAdapter
import store.controller.adapter.PromotionAdapter
import store.controller.domain.UserInteractionController
import store.controller.validator.BuyingValidator
import store.controller.validator.YesOrNoValidator
import store.model.Product
import store.util.retryWhenNoException
import store.view.InputView

class StoreController(
    private val promotionAdapter: PromotionAdapter = PromotionAdapter(),
    private val productAdapter: ProductAdapter = ProductAdapter(),
    private val userInteractionController: UserInteractionController = UserInteractionController(),
    private val buyingValidator: BuyingValidator = BuyingValidator(),
    private val inputView: InputView = InputView(),
    private val yesOrNoValidator: YesOrNoValidator = YesOrNoValidator(),
) {
    fun run() {
        val promotions = promotionAdapter.loadPromotion()
        val storageProducts = productAdapter.run(promotions)
        val userBuying = getUserBuying(storageProducts)
        val userBuyingProducts = parseUserBuying(userBuying)
        val promotion1 = checkPromotionQuantity(storageProducts, userBuyingProducts)
    }

    private fun getUserBuying(storageProducts: List<Product>): String {
        val userBuying = retryWhenNoException {
            val userBuying = userInteractionController.handleWelcomeMsg(storageProducts)
            buyingValidator(userBuying, storageProducts)
            userBuying
        }
        return userBuying
    }

    private fun parseUserBuying(userBuying: String): List<List<String>> {
        val buyingProducts = userBuying.split(",").map { it.trim() }
        val userBuyingProducts = buyingProducts.map { it.removeSurrounding("[", "]").split("-") }
        return userBuyingProducts
    }

    private fun checkPromotionQuantity(
        storageProducts: List<Product>,
        userBuyingProducts: List<List<String>>
    ): List<List<String>> {
        val updatedUserBuyingProducts = mutableListOf<List<String>>()
        for (userBuyingProduct in userBuyingProducts) {
            val product = storageProducts.find { it.name == userBuyingProduct[0] }
            val promotionCount = product?.checkPromotionQuantity(userBuyingProduct[1].toInt())
            if (promotionCount != 0) {
                println("현재 ${userBuyingProduct[0]}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)")
                val yesOrNo = getYesOrNo()
                if (yesOrNo == "Y") {
                    val newQuantity = (userBuyingProduct[1].toInt() + 1).toString()
                    updatedUserBuyingProducts.add(listOf(userBuyingProduct[0], newQuantity))
                    continue
                }
            }
            updatedUserBuyingProducts.add(userBuyingProduct)
        }
        return updatedUserBuyingProducts
    }

    private fun getYesOrNo(): String {
        val yesOrNo = retryWhenNoException {
            val yesOrNo = inputView.getInput()
            yesOrNoValidator(yesOrNo)
            yesOrNo
        }
        return yesOrNo
    }
}