package store.model

data class Product(
    val name: String,
    val price: Int,
    val quantity: Int,
    val promotion: Promotion?
)
