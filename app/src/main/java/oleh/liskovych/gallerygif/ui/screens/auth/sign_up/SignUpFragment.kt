package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.include_auth_pack.*
import oleh.liskovych.gallerygif.BuildConfig
import oleh.liskovych.gallerygif.FILE_PROVIDER_NAME
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.getStringText
import oleh.liskovych.gallerygif.extensions.hideInputFieldsErrors
import oleh.liskovych.gallerygif.extensions.markFieldsRequired
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import oleh.liskovych.gallerygif.ui.base.RequestCode
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureDialog
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureType
import oleh.liskovych.gallerygif.utils.ImageUtils
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import org.jetbrains.anko.support.v4.ctx

class SignUpFragment : BaseFragment<SignUpViewModel>(), View.OnClickListener {

    override val viewModelClass = SignUpViewModel::class.java
    override val layoutId = R.layout.fragment_sign_up

    private val rxPermissions by lazy { RxPermissions(this) }
    private val disposable = CompositeDisposable()
    private var callback: SignUpCallback? = null
    private var currentPhotoPath: String? = null // todo: consider moving to viewModel

    private val validationObserver = Observer<Boolean> {
        if (it) navigateToMain()
    }

    private val emailObserver = Observer<ValidationResponse> {
        showValidationError(etEmail, it)
    }

    private val passwordObserver = Observer<ValidationResponse> {
        showValidationError(etPassword, it)
    }

    override fun observeLiveData(viewModel: SignUpViewModel) {
        with(viewModel) {
            isValid.observe(this@SignUpFragment, validationObserver)
            isEmailValid.observe(this@SignUpFragment, emailObserver)
            isPasswordsValid.observe(this@SignUpFragment, passwordObserver)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivProfilePicture, bSend)
        markFieldsRequired(tilEmail, tilPassword)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivProfilePicture -> fragmentManager?.let {
                TakePictureDialog.newInstance(this, RequestCode.REQUEST_DIALOG_TAKE_PICTURE())
                    .show(it, TakePictureDialog::class.java.simpleName)
            }
            R.id.bSend -> validateFields()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    RequestCode.REQUEST_DIALOG_TAKE_PICTURE() ->
                        data?.let { dialogTakePictureResult(it.getSerializableExtra(TakePictureDialog.TAKE_PICTURE_EXTRA) as TakePictureType) }
                    RequestCode.REQUEST_CAPTURE_PICTURE_WITH_CAMERA() -> capturePictureWithCameraResult()
                }
            }
        }
    }

    private fun dialogTakePictureResult(type: TakePictureType) {
        when (type) {
            TakePictureType.CAMERA -> capturePictureWithCamera()
            TakePictureType.GALLERY -> pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {

    }

    private fun capturePictureWithCamera() {
        disposable.add(rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted ->
                if (granted) {
                    ImageUtils.createImagePickIntentFromCamera(
                        ctx,
                        { ImageUtils.createImageFileTemp(ctx, false).also { currentPhotoPath } }, FILE_PROVIDER_NAME)
                        ?.let { startActivityForResult(it, RequestCode.REQUEST_CAPTURE_PICTURE_WITH_CAMERA()) }
                }
            })
    }

    private fun capturePictureWithCameraResult() {
        currentPhotoPath?.let {  }
    }

    private fun showValidationError(textInput: TextInputEditText, response: ValidationResponse) {
        response.run { if (!isValid) showErrorInField(textInput, errorMessage) }
    }

    private fun showErrorInField(textInput: TextInputEditText, error: String?) {
        textInput.error = error
    }

    private fun validateFields() {
        hideInputFieldsErrors(etEmail, etPassword)
        validateUserData()
    }

    private fun validateUserData() = viewModel.validateUserData(
        etEmail.getStringText(), etPassword.getStringText()
    )

    private fun navigateToMain() {
        findNavController().navigate(R.id.to_main, null, null)
        callback?.finishActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<SignUpCallback>(context, parentFragment)
    }

    override fun onDetach() {
        callback = null
        disposable.dispose()
        super.onDetach()
    }

}