package store.model

data class PromotionProduct(
    val price: Int,
    val quantity: Int,
    val promotion: Promotion,
)
