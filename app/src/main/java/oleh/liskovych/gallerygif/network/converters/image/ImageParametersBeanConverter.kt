package oleh.liskovych.gallerygif.network.converters.image

import oleh.liskovych.gallerygif.models.ImageParameters
import oleh.liskovych.gallerygif.models.ImageParametersModel
import oleh.liskovych.gallerygif.models.converters.BaseConverter
import oleh.liskovych.gallerygif.network.bean.images.ImageParametersBean

// Redundant in this project. Added just for expanding ability demonstration
class ImageParametersBeanConverter: BaseConverter<ImageParametersBean, ImageParameters>() {

    override fun processConvertedInOut(inObject: ImageParametersBean): ImageParameters  = inObject.run {
        ImageParametersModel(address, latitude, longitude, weather)
    }

    override fun processConvertOutToIn(outObject: ImageParameters): ImageParametersBean = outObject.run {
        ImageParametersBean(address, latitude, longitude, weather)
    }
}