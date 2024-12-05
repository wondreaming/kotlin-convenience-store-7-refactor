package store.controller.validator

import store.model.Product

class BuyingValidator {
    operator fun invoke(input: String, storageProducts: List<Product>) {
        isEmpty(input)
        val products = input.split(",").map { it.trim() }
        for (product in products) {
            isBracket(product)
            val (name, quantity) = product.removeSurrounding("[", "]").split("-")
            isStorage(name, storageProducts)
            isInteger(quantity)
            isNegativeNumber(quantity)
            isQuantity(name, quantity, storageProducts)
        }
    }

    private fun isEmpty(input: String) {
        require(input.isNotEmpty()) { BuyingErrorType.WRONG_INPUT }
    }

    private fun isBracket(input: String) {
        require(input.contains("[") && input.contains("]") && input.contains("-")) { BuyingErrorType.WRONG_FORMAT }
    }

    private fun isInteger(input: String) {
        requireNotNull(input.toIntOrNull()) { BuyingErrorType.WRONG_FORMAT }
    }

    private fun isStorage(input: String, storageProducts: List<Product>) {
        require(storageProducts.find { it.name == input } != null) { BuyingErrorType.NOT_EXIST }
    }

    private fun isNegativeNumber(input: String) {
        val quantity = input.toInt()
        require(quantity > 0) { BuyingErrorType.WRONG_FORMAT }
    }

    private fun isQuantity(name: String, quantity: String, storageProducts: List<Product>) {
        var realQuality = 0
        for (storageProduct in storageProducts) {
            if (storageProduct.name == name) {
                realQuality += storageProduct.quantity
            }
        }
        require(quantity.toInt() <= realQuality) { BuyingErrorType.OVER }
    }
}