package oleh.liskovych.gallerygif.network.error.validation

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationErrorChildren(@JsonProperty("username")
                                   var username: ValidationErrorsList? = null,
                                   @JsonProperty("email")
                                   var email: ValidationErrorsList? = null,
                                   @JsonProperty("password")
                                   var password: ValidationErrorsList? = null,
                                   @JsonProperty("avatar")
                                   var avatar: ValidationErrorsList? = null
)