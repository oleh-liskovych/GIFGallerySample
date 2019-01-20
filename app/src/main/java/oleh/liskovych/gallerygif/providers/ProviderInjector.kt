package oleh.liskovych.gallerygif.providers

object ProviderInjector {
    val userProvider: UserProvider by lazy { UserProvider() }

    val imageProvider: ImageProvider by lazy { ImageProvider() }
}