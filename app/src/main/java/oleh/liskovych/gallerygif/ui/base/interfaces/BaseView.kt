package oleh.liskovych.gallerygif.ui.base.interfaces

import android.view.View
import androidx.annotation.StringRes


interface BaseView : ProgressView {

    fun showSnackBar(@StringRes res: Int)

    fun showSnackBar(text: String?)

    fun showSnackBar(rootView: View?, @StringRes res: Int)

    fun showSnackBar(rootView: View?, text: String?)

}