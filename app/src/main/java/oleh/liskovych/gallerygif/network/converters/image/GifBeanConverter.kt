package oleh.liskovych.gallerygif.network.converters.image

import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.models.GifModel
import oleh.liskovych.gallerygif.models.converters.BaseConverter
import oleh.liskovych.gallerygif.network.bean.GifBean

// Redundant in this project. Added just for expanding ability demonstration
class GifBeanConverter: BaseConverter<GifBean, Gif>() {

    override fun processConvertedInOut(inObject: GifBean): Gif = inObject.run {
        GifModel(gif)
    }

    override fun processConvertOutToIn(outObject: Gif): GifBean = outObject.run {
        GifBean(gifPath)
    }
}