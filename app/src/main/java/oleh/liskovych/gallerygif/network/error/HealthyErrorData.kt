package oleh.liskovych.gallerygif.network.error

import com.fasterxml.jackson.annotation.JsonProperty

data class HealthyErrorData(@JsonProperty("code")
                            val code: Int? = null,
                            @JsonProperty("message")
                            val message: String? = null)