package oleh.liskovych.gallerygif.network.request

import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.JsonKeywords.EMAIL
import oleh.liskovych.gallerygif.JsonKeywords.PASSWORD

data class SignInRequest(@field:JsonProperty(EMAIL)
                         val email: String,
                         @field:JsonProperty(PASSWORD)
                         val password: String)