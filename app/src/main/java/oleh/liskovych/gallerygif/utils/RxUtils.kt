package oleh.liskovych.gallerygif.utils

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> ioToMain() = FlowableTransformer<T, T> {
    it
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}