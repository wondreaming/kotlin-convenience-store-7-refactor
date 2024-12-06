package store.model

data class Product(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: Promotion?
) {
    fun checkPromotionQuantity(userQuantity: Int): Int {
        if (promotion != null && promotion.isActive()) {
            val promotionQuantity = promotion.buy + promotion.get
            val missingQuantity = userQuantity % promotionQuantity
            return (promotionQuantity - missingQuantity).takeIf { missingQuantity == promotion.buy && quantity >= userQuantity + missingQuantity }
                ?: 0
        }
        return 0
    }

    fun reduceQuantity(count: Int): Int {
        if (quantity - count >= 0) {
            quantity -= count
            return 0
        } else {
            val result = count - quantity
            quantity = 0
            return result
        }
    }
}
