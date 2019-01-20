package oleh.liskovych.gallerygif.ui.base

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import oleh.liskovych.gallerygif.extensions.hideKeyboard
import oleh.liskovych.gallerygif.ui.base.interfaces.BackPressable
import oleh.liskovych.gallerygif.ui.base.interfaces.BackPressedCallback
import oleh.liskovych.gallerygif.ui.base.interfaces.BaseView
import oleh.liskovych.gallerygif.ui.base.interfaces.ProgressNavigationCallback
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow

abstract class BaseFragment<T : BaseViewModel> : Fragment(),
    BaseView,
    BackPressable {

    abstract val viewModelClass: Class<T>

    private val textWatchers: Map<EditText?, TextWatcher> = mutableMapOf()

    protected val viewModel: T by lazy (LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(viewModelClass)
    }

    protected abstract val layoutId: Int

    private var baseView: BaseView? = null

    private var backPressedCallback: BackPressedCallback? = null

    private var progressNavigationCallback: ProgressNavigationCallback? = null

    private val progressObserver = Observer<Boolean> {
        if (it) showProgress() else hideProgress()
    }

    private val errorObserver = Observer<String> {showSnackBar(it)}

    abstract fun observeLiveData(viewModel: T)



    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseView = bindInterfaceOrThrow<BaseView>(parentFragment, context)
        backPressedCallback = bindInterfaceOrThrow<BackPressedCallback>(parentFragment, context)
    }

    override fun onDetach() {
        baseView = null
        backPressedCallback = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAllLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hideKeyboard()
        return inflater.inflate(layoutId, container, false)
    }

    fun EditText.addTextWatcher(watcher: TextWatcher) = this.apply {
        textWatchers.plus(this to watcher)
        addTextChangedListener(watcher)
    }

    fun backPressed() {
        backPressedCallback?.backPressed()
    }

    override fun showProgress() {
        baseView?.showProgress()
    }

    override fun hideProgress() {
        baseView?.hideProgress()
    }

    override fun showSnackBar(@StringRes res: Int) {
        baseView?.showSnackBar(view, res)
    }

    override fun showSnackBar(text: String?) {
        baseView?.showSnackBar(view, text)
    }

    override fun showSnackBar(rootView: View?, @StringRes res: Int) {
        baseView?.showSnackBar(rootView, getString(res))
    }

    override fun showSnackBar(rootView: View?, text: String?) {
        baseView?.showSnackBar(rootView, text)
    }

    protected open fun blockBackAction(): Boolean = false

    override fun onBackPressed(): Boolean =
        when (blockBackAction()) {
            true -> {
                progressNavigationCallback?.blockBackAction()
                blockBackAction()
            }
            else -> blockBackAction()
        }

    private fun observeAllLiveData() {
        observeLiveData(viewModel)
        with(viewModel) {
            isLoadingLiveData.observe(this@BaseFragment, progressObserver)
            errorLiveData.observe(this@BaseFragment, errorObserver)

            setLoadingLiveData(errorLiveData)
        }
    }
}