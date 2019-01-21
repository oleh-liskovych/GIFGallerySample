package oleh.liskovych.gallerygif.network.error

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorTypeOne(@JsonProperty("error")
                        val error: String? = null)