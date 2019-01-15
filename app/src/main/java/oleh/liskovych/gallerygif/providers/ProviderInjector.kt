package oleh.liskovych.gallerygif.providers

object ProviderInjector {

    fun getUserProvider() = UserProvider()

    fun getImageProvider() = ImageProvider()
}