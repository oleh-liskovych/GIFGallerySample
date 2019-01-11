package oleh.liskovych.gallerygif.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import oleh.liskovych.gallerygif.utils.withNotNull

abstract class BaseDialogFragment : DialogFragment() {

    protected abstract val layoutId: Int

    protected abstract val gravity: Int

    protected abstract val dialogWidth: Int

    protected abstract val dialogHeight: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }

    override fun onResume() {
        super.onResume()
        withNotNull(dialog?.window) {
            attributes.gravity = gravity
            setLayout(dialogWidth, dialogHeight)
        }
    }
}