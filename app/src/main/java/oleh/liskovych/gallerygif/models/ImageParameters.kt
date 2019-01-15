package oleh.liskovych.gallerygif.models

import kotlinx.android.parcel.Parcelize

interface ImageParameters: Model {
    var address: String?
    var latitude: Float?
    var longitude: Float?
    var weather: String?
}

@Parcelize
class ImageParametersModel(override var address: String? = null,
                           override var latitude: Float? = null,
                           override var longitude: Float? = null,
                           override var weather: String? = null): ImageParameters