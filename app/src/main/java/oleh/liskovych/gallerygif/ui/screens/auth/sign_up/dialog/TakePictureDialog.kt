package oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dialog_take_picture.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.ui.base.BaseDialogFragment
import oleh.liskovych.gallerygif.utils.getDimensionPixelSizeApp

class TakePictureDialog: BaseDialogFragment(), View.OnClickListener {

    companion object {
        val TAKE_PICTURE_EXTRA = TakePictureDialog::class.java.simpleName

        fun newInstance(targetFragment: Fragment, requestCode: Int) =
            TakePictureDialog().apply { setTargetFragment(targetFragment, requestCode) }
    }

    override val layoutId = R.layout.dialog_take_picture
    override val gravity = Gravity.BOTTOM
    override val dialogWidth = ViewGroup.LayoutParams.MATCH_PARENT
    override val dialogHeight= ViewGroup.LayoutParams.WRAP_CONTENT

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.TakePictureDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(tvCamera, tvGallery)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.tvCamera -> deliveryResult(TakePictureType.CAMERA)
            R.id.tvGallery -> deliveryResult(TakePictureType.GALLERY)
        }
        dismiss()
    }

    private fun deliveryResult(type: TakePictureType) {
        targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, Intent().putExtra(TAKE_PICTURE_EXTRA, type))
    }
}