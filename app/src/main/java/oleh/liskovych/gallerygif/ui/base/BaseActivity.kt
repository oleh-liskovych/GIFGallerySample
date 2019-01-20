package oleh.liskovych.gallerygif.ui.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.SNACK_BAR_MAX_LINES
import oleh.liskovych.gallerygif.extensions.hide
import oleh.liskovych.gallerygif.extensions.hideKeyboard
import oleh.liskovych.gallerygif.extensions.show
import oleh.liskovych.gallerygif.ui.base.interfaces.BackPressedCallback
import oleh.liskovych.gallerygif.ui.base.interfaces.BaseView
import org.jetbrains.anko.find

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(),
    BaseView,
    BackPressedCallback {

    abstract val viewModelClass: Class<T>

    protected abstract val layoutId: Int

    private var vProgress: View? = null

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }

    protected open var navHostFragmentId = R.id.nav_host_fragment

    protected open fun hasProgressBar(): Boolean = false

    private fun getProgressBarId() = R.id.progressView

    private val progressObserver = Observer<Boolean> {
        if (it) showProgress() else hideProgress()
    }

    private val errorObserver = Observer<String> {
        showSnackBar(it)
    }

    abstract fun observeLiveData(viewModel: T)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        if (hasProgressBar()) vProgress = find(getProgressBarId())
        setupNavigation()
        observeAllLiveData()
    }

    override fun showSnackBar(text: String?) {
        showSnackBar(find(android.R.id.content), text)
    }

    override fun showSnackBar(@StringRes res: Int) {
        showSnackBar(find(android.R.id.content), res)
    }

    override fun backPressed() { // todo: replace it with something more Navigationable
        with(supportFragmentManager) {
            backStackEntryCount.takeUnless { it == 0 }?.let { popBackStack() } ?: onBackPressed()
        }
    }

    override fun showProgress() {
        vProgress?.show()
    }

    override fun hideProgress() {
        vProgress?.hide()
    }

    private fun setupNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(navHostFragmentId) as NavHostFragment? ?: return

        host.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

//            Toast.makeText(this, "Navigated to $dest",
//                Toast.LENGTH_SHORT).show() // todo: remove
        }
    }

    override fun showSnackBar(rootView: View?, @StringRes res: Int) {
        showSnackBar(rootView, getString(res))
    }

    override fun showSnackBar(rootView: View?, text: String?) {
        text?.let { txt ->
            rootView?.let {
                hideKeyboard()
                Snackbar.make(it, txt, Snackbar.LENGTH_LONG)
                    .apply {
                        view.setUpSnackBarView(this)
                        show()
                    }
            }
        }
    }

    private fun observeAllLiveData() {
        observeLiveData(viewModel)
        with(viewModel) {
            isLoadingLiveData.observe(this@BaseActivity, progressObserver)
            errorLiveData.observe(this@BaseActivity, errorObserver)
        }
    }

    private fun View.setUpSnackBarView(snackbar: Snackbar) = with(this) {
        setOnClickListener { snackbar.dismiss() }
        with(find<View>(R.id.snackbar_text) as TextView) {
            maxLines = SNACK_BAR_MAX_LINES
        }
    }


}