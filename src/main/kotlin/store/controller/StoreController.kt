package store.controller

import store.controller.adapter.ProductAdapter
import store.controller.adapter.PromotionAdapter
import store.controller.domain.UserInteractionController
import store.controller.validator.BuyingValidator
import store.controller.validator.YesOrNoValidator
import store.model.Product
import store.util.DecimalUtil.getDecimalFormat
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
        var again = true
        val promotions = promotionAdapter.loadPromotion()
        var storageProducts = productAdapter.run(promotions)
        while (again) {
            val userBuying = getUserBuying(storageProducts)
            val userBuyingProducts = parseUserBuying(userBuying)
            val promotion1 = checkPromotionQuantity(storageProducts, userBuyingProducts)
            val promotion2 = checkNonPromotion(storageProducts, promotion1)
            val isMembership = getMembership()
            storageProducts = printReceipt(storageProducts, promotion2, isMembership)
            again = buyAgain()
        }
    }

    private fun getMembership(): Boolean {
        println("멤버십 할인을 받으시겠습니까? (Y/N)")
        val yesOrNo = getYesOrNo()
        return yesOrNo == "Y"
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

    private fun checkNonPromotion(
        storageProducts: List<Product>,
        userBuyingProducts: List<List<String>>
    ): List<List<String>> {
        val updatedUserBuyingProducts = mutableListOf<List<String>>()
        for (userBuyingProduct in userBuyingProducts) {
            val product = storageProducts.find { it.name == userBuyingProduct[0] && it.promotion != null }
            if (product != null && product.quantity < userBuyingProduct[1].toInt()) {
                val quantity = userBuyingProduct[1].toInt() - product.quantity
                println("현재 ${userBuyingProduct[0]} ${quantity}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)")
                val yesOrNo = getYesOrNo()
                if (yesOrNo == "N") {
                    val newQuantity = (userBuyingProduct[1].toInt() - quantity).toString()
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

    private fun printReceipt(storageProducts: List<Product>, products: List<List<String>>, isMember: Boolean): List<Product> {
        var totalPrice = 0
        var totalAmount = 0
        var promotionPrice = 0
        println("===========W 편의점=============")
        println("상품명\t\t수량\t금액")
        for (product in products) {
            val price = storageProducts.find { it.name == product[0] }?.price
            val pay = getDecimalFormat(price!! * product[1].toInt())
            println("${product[0]} ${product[1]} ${pay}")
            totalPrice += price * product[1].toInt()
            totalAmount += product[1].toInt()
            storageProducts.find { it.name == product[0] }!!.reduceQuantity(product[1].toInt())
        }
        println("===========증\t정=============")
        for (product in products) {
            val promotionProduct = storageProducts.find { it.name == product[0] }
            if (promotionProduct?.promotion != null && promotionProduct.promotion.isActive()) {
                val buy = storageProducts.find { it.name == product[0] }?.promotion?.buy
                val free = product[1].toInt() / buy!!
                println("${product[0]} $free")
                promotionPrice += storageProducts.find { it.name == product[0] }?.price!! * free
            }
        }
        var memberPrice = (Math.floor((totalPrice - promotionPrice) * 0.3 / 1000) * 1000).toInt()
        if (memberPrice > 8000) {
            memberPrice = 8000
        }
        println("==============================")
        println("총구매액 ${totalAmount} ${getDecimalFormat(totalPrice)}")
        println("행사할인 -${getDecimalFormat(promotionPrice)}")
        if (isMember) println("멤버십할인 -${memberPrice}") else println("멤버십할인 -0")
        if (isMember) println("내실돈 ${getDecimalFormat((totalPrice - promotionPrice - memberPrice).toInt())}") else println(
            "내실돈 ${getDecimalFormat(totalPrice - promotionPrice)}"
        )

        return storageProducts
    }

    private fun buyAgain(): Boolean {
        println("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)")
        val yesOrNo = getYesOrNo()
        return yesOrNo == "Y"
    }
}