package store.model

data class Product(
    val name: String,
    val promotionProduct: PromotionProduct?,
    val nonPromotionProduct: NonPromotionProduct?,
) {
}
