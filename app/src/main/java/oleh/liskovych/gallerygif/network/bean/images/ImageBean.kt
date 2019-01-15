package oleh.liskovych.gallerygif.network.bean.images

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.DATETIME_PATTERN
import oleh.liskovych.gallerygif.JsonKeywords.BIG_IMAGE_PATH
import oleh.liskovych.gallerygif.JsonKeywords.CREATED
import oleh.liskovych.gallerygif.JsonKeywords.ID
import oleh.liskovych.gallerygif.JsonKeywords.PARAMETERS
import oleh.liskovych.gallerygif.JsonKeywords.SMALL_IMAGE_PATH
import org.joda.time.DateTime

data class ImageBean(@JsonProperty(ID)
                     val id: Long?,
                     @JsonProperty(PARAMETERS)
                     val parameters: ImageParametersBean?,
                     @JsonProperty(SMALL_IMAGE_PATH)
                     val smallImage: String?,
                     @JsonProperty(BIG_IMAGE_PATH)
                     val bigImage: String?,
                     @JsonProperty(CREATED)
                     @JsonFormat(pattern = DATETIME_PATTERN)
                     val created: DateTime?)