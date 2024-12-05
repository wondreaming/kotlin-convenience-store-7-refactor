package store.model

data class Product(
    val name: String,
    val price: Int,
    val quantity: Int,
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
}
