package oleh.liskovych.gallerygif

const val SNACK_BAR_MAX_LINES = 5
const val EMPTY_STRING = ""
const val SPACE_STRING = " "
const val FILE_PROVIDER_NAME = "oleh.liskovych.gallerygif.fileprovider"
const val DATETIME_PATTERN_USER = "yyyy-MM-dd HH:mm:ss"
const val DATETIME_PATTERN_IMAGE = "dd-MM-yyyy HH:mm:ss"

object JsonKeywords {
    const val USERNAME = "username"
    const val EMAIL = "email"
    const val PASSWORD = "password"
    const val CREATION_TIME = "creation_time"
    const val TOKEN = "token"
    const val ID = "id"
    const val IMAGE = "image"
    const val IMAGES = "images"
    const val AVATAR = "avatar"
    const val DESCRIPTION = "description"
    const val HASHTAG = "hashtag"
    const val PARAMETERS = "parameters"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val WEATHER = "weather"
    const val GIF = "gif"
    const val ADDRESS = "address"
    const val SMALL_IMAGE_PATH = "smallImagePath"
    const val BIG_IMAGE_PATH = "bigImagePath"
    const val CREATED = "created"
}

object Endpoints {
    const val LOGIN = "login"
    const val CREATE = "create"
    const val ALL = "all"
    const val IMAGE = "image"
    const val GIF = "gif"
}