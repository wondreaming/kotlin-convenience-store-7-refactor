package store.controller.validator

enum class YesOrNoErrorType (
    private val errorMessage: String
) {
    WRONG_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    override fun toString(): String = "$ERROR $errorMessage"

    companion object {
        private const val ERROR = "[ERROR]"
    }
}