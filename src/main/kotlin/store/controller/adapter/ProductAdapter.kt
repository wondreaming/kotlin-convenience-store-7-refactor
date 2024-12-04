package store.controller.adapter

import store.model.NonPromotionProduct
import store.model.Product
import store.model.Promotion
import store.model.PromotionProduct
import java.io.File

class ProductAdapter {
    fun loadProductType(promotions: List<Promotion>): List<Product> {
        val products = File("src/main/resources/products.md").readLines().filterNot { it.startsWith("~~name") }.map {
            parseProduct(it, promotions)
        }
        return products
    }

    private fun parseProduct(line: String, promotions: List<Promotion>): Product {
        val (name, price, quantity, promotion) = line.split(",")
        return when (promotion) {
            "null" -> {
                val nonPromotionProduct = NonPromotionProduct(price.toInt(), quantity.toInt())
                Product(name, null, nonPromotionProduct)
            }

            else -> {
                val productOfPromotion = promotions.find { it.name == promotion }
                val promotionProduct = PromotionProduct(price.toInt(), quantity.toInt(), productOfPromotion!!)
                return Product(
                    name, promotionProduct, null
                )
            }
        }
    }
}