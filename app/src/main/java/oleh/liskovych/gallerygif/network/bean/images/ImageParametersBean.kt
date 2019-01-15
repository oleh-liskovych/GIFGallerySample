package oleh.liskovych.gallerygif.network.bean.images

import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.JsonKeywords.ADDRESS
import oleh.liskovych.gallerygif.JsonKeywords.LATITUDE
import oleh.liskovych.gallerygif.JsonKeywords.LONGITUDE
import oleh.liskovych.gallerygif.JsonKeywords.WEATHER

data class ImageParametersBean(@JsonProperty(ADDRESS)
                               val address: String?,
                               @JsonProperty(LATITUDE)
                               val latitude: Float?,
                               @JsonProperty(LONGITUDE)
                               val longitude: Float?,
                               @JsonProperty(WEATHER)
                               val weather: String?)