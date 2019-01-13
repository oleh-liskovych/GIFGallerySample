package oleh.liskovych.gallerygif.network.converters

import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.models.UserModel
import oleh.liskovych.gallerygif.models.converters.BaseConverter
import oleh.liskovych.gallerygif.network.bean.UserBean

// Redundant in this project. Added just for expanding ability demonstration
class UserBeanConverter: BaseConverter<UserBean, User>() {

    override fun processConvertedInOut(inObject: UserBean): User = inObject.run {
        UserModel(creationTime, token, avatar)
    }

    override fun processConvertOutToIn(outObject: User): UserBean = outObject.run {
        UserBean(creationTime, token, avatar)
    }

}