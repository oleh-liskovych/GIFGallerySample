package oleh.liskovych.gallerygif.network.bean.images

import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.JsonKeywords.IMAGES

data class ImagesContainerBean(@JsonProperty(IMAGES)
                          val images: List<ImageBean>?)