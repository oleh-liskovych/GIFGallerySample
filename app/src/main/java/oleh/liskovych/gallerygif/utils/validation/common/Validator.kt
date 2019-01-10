package oleh.liskovych.gallerygif.utils.validation.common

interface Validator {

    fun validate(value: String?): ValidationResponse

}