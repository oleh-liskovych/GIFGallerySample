package oleh.liskovych.gallerygif.network.error.validation

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationError(@JsonProperty("children")
                           var children: ValidationErrorChildren)