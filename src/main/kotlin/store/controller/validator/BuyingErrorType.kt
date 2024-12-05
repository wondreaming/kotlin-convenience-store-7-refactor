package store.controller.validator

enum class BuyingErrorType(
    private val errorMessage: String
) {
    WRONG_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NOT_EXIST("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    OVER("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    WRONG_INPUT("잘못된 입력입니다. 다시 입력해 주세요.");

    override fun toString(): String = "$ERROR $errorMessage"

    companion object {
        private const val ERROR = "[ERROR]"
    }
}