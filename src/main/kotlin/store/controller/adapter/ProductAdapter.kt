package store.controller.adapter

import store.model.Product
import store.model.Promotion
import java.io.File

class ProductAdapter {
    fun run(promotions: List<Promotion>): List<Product> {
        val products = loadProduct(promotions)
        val newProducts = organizeProducts(products)
        return newProducts
    }

    private fun loadProduct(promotions: List<Promotion>): List<Product> {
        return File("src/main/resources/products.md").readLines().filterNot { it.startsWith("name") }
            .map { parseProduct(it, promotions) }
    }

    private fun parseProduct(input: String, promotions: List<Promotion>): Product {
        val (name, price, quantity, promotion) = input.split(",")
        val promotionInfo = if (promotion != "null") {
            promotions.find { it.name == promotion }
        } else null
        return Product(name, price.toInt(), quantity.toInt(), promotionInfo)
    }

    private fun organizeProducts(products: List<Product>): List<Product> {
        val newProducts = mutableListOf<Product>()
        for (product in products) {
            newProducts.add(product)
            if (product.promotion != null) {
                val target = products.find { it.name == product.name && it.promotion == null }
                if (target == null) {
                    newProducts.add(Product(product.name, product.price, 0, null))
                }
            }
        }
        return newProducts
    }
}