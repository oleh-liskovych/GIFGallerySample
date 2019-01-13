package oleh.liskovych.gallerygif.network.error.validation

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationErrorsList(@JsonProperty("errors")
                                var errors: List<String>? = null)