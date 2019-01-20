package oleh.liskovych.gallerygif.ui.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import oleh.liskovych.gallerygif.EMPTY_STRING
import oleh.liskovych.gallerygif.R
import org.jetbrains.anko.find

abstract class BaseToolbarFragment<T : BaseViewModel> : BaseFragment<T>() {

    companion object {
        const val NO_TITLE = -1
    }

    protected var toolbar: Toolbar? = null

    @get:IdRes
    protected abstract val toolbarId: Int

    @get:StringRes
    protected abstract val screenTitleRes: Int

    protected abstract val showToolbarBackArrow: Boolean

    protected open var backNavigationIcon: Int = R.drawable.ic_back_arrow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        initToolbar()
    }

    private fun initToolbar() {
        toolbar = view?.find(toolbarId)
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                setupActionBar(it)
                if (showToolbarBackArrow) {
                    toolbar?.run {
                        setNavigationIcon(backNavigationIcon)
                        setNavigationOnClickListener { _ -> onBackPressed() }
                    }
                }
            }
        }
    }

    private fun getStringScreenTitle(@StringRes titleRes: Int) =
        if (titleRes != NO_TITLE) getString(titleRes) else EMPTY_STRING


    protected fun setupActionBar(actionBar: ActionBar) {
        actionBar.apply {
            title = getString(screenTitleRes)
            setDisplayHomeAsUpEnabled(showToolbarBackArrow)
        }
    }

}






























