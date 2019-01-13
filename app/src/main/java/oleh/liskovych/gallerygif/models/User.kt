package oleh.liskovych.gallerygif.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime

interface User: Model {
    var creationTime: DateTime?
    var token: String?
    var avatar: String?
}

@Parcelize
class UserModel(override var creationTime: DateTime? = null,
                override var token: String? = null,
                override var avatar: String? = null) : User, Parcelable