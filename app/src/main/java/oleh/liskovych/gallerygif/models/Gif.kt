package oleh.liskovych.gallerygif.models

import kotlinx.android.parcel.Parcelize

interface Gif: Model {
    var gifPath: String?
}

@Parcelize
class GifModel(override var gifPath: String?): Gif