package oleh.liskovych.gallerygif.network.error

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorTypeTwo(@JsonProperty("error")
                        val error: ErrorTypeTwoData? = null)