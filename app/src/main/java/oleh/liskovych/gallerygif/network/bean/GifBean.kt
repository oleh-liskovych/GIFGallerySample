package oleh.liskovych.gallerygif.network.bean

import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.JsonKeywords.GIF

data class GifBean(@JsonProperty(GIF)
                   val gif: String?)