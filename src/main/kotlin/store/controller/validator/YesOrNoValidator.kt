package store.controller.validator

class YesOrNoValidator {
    operator fun invoke(input: String) {
        isEmpty(input)
        isYesOrNo(input)
    }

    private fun isEmpty(input: String) {
        require(input.isNotEmpty()) { YesOrNoErrorType.WRONG_FORMAT }
    }

    private fun isYesOrNo(input: String) {
        require(input == "Y" || input == "N") { YesOrNoErrorType.WRONG_FORMAT }
    }
}