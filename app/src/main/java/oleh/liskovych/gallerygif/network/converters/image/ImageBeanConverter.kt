package oleh.liskovych.gallerygif.network.converters.image

import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.models.ImageModel
import oleh.liskovych.gallerygif.models.converters.BaseConverter
import oleh.liskovych.gallerygif.network.bean.images.ImageBean

// Redundant in this project. Added just for expanding ability demonstration
class ImageBeanConverter: BaseConverter<ImageBean, Image>() {

    private val imageParametersBeanConverter = ImageParametersBeanConverter()

    override fun processConvertedInOut(inObject: ImageBean): Image = inObject.run {
        ImageModel(id, parameters?.let { imageParametersBeanConverter.convertInToOut(it) }, smallImage, bigImage, created)
    }

    override fun processConvertOutToIn(outObject: Image): ImageBean = outObject.run {
        ImageBean(id, parameters?.let { imageParametersBeanConverter.convertOutToInt(it) }, smallImagePath, bigImagePath, created)
    }
}