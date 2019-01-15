package oleh.liskovych.gallerygif.providers.base

import oleh.liskovych.gallerygif.models.Model

abstract class BaseOnlineProvider<M: Model, NetworkModule>: Provider<M> {

    val networkModule: NetworkModule = this.initNetworkModule()

    protected abstract fun initNetworkModule(): NetworkModule

}