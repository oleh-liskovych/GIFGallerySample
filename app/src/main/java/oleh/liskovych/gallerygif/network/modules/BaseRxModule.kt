package oleh.liskovych.gallerygif.network.modules

import oleh.liskovych.gallerygif.models.converters.Converter


abstract class BaseRxModule<T, NetworkModel, M>(val api: T, val converter: Converter<NetworkModel, M>)