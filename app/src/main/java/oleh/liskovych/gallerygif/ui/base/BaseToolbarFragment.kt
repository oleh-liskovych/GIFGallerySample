package oleh.liskovych.gallerygif.ui.base

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import oleh.liskovych.gallerygif.EMPTY_STRING_VALUE

abstract class BaseToolbarFragment<T : BaseViewModel>: BaseFragment<T>() {

    companion object {
        const val NO_TITLE = -1
    }

    protected var toolbar: Toolbar? = null

    @IdRes
    protected abstract fun getToolbarId(): Int

    @StringRes
    protected abstract fun getScreenTitle(): Int

    protected open fun needToShowBackNav() = true

    protected fun getStringScreenTitle() =
        if (getScreenTitle() != NO_TITLE) {
            getString(getScreenTitle())
        } else {
            EMPTY_STRING_VALUE
        }

    private fun initToolbar() {
        view?.apply {}
    }

}