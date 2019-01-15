package oleh.liskovych.gallerygif.models

import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

interface Image: Model {
    var id: Long?
    var parameters: ImageParameters?
    var smallImagePath: String?
    var bigImagePath: String?
    var created: DateTime?
}

@Parcelize
class ImageModel(override var id: Long? = null,
                 override var parameters: ImageParameters? = null,
                 override var smallImagePath: String? = null,
                 override var bigImagePath: String? = null,
                 override var created: DateTime? = null): Image