package oleh.liskovych.gallerygif.utils

import androidx.annotation.DimenRes
import oleh.liskovych.gallerygif.App
import oleh.liskovych.gallerygif.ui.base.NotImplementedInterfaceException

inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?): T = objects.find { it is T }
    ?.let { it as T }
    ?: throw NotImplementedInterfaceException(T::class.java)

fun getDimensionPixelSizeApp(@DimenRes dimenRes: Int) = App.instance.resources.getDimensionPixelSize(dimenRes)

inline fun <T, R> withNotNull(receiver: T?, block: T.() -> R): R? = receiver?.block()