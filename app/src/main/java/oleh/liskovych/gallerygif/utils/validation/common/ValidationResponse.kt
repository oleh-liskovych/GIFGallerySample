package oleh.liskovych.gallerygif.utils.validation.common

interface ValidationResponse {
    val isValid: Boolean
    val errorMessage: String?
}