package oleh.liskovych.gallerygif.network.error

import com.fasterxml.jackson.annotation.JsonProperty

data class HealthyError(@JsonProperty("error")
                        val error: HealthyErrorData? = null)