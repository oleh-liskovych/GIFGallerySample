package oleh.liskovych.gallerygif.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SignInRequest(@field:JsonProperty("email")
                         val email: String,
                         @field:JsonProperty("password")
                         val password: String)