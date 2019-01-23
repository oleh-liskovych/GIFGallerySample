package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.include_auth_pack.*
import oleh.liskovych.gallerygif.FILE_PROVIDER_NAME
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.*
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import oleh.liskovych.gallerygif.ui.base.RequestCode
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureDialog
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureType
import oleh.liskovych.gallerygif.utils.FileUtils.pickedExistingPicture
import oleh.liskovych.gallerygif.utils.ImageUtils
import oleh.liskovych.gallerygif.utils.ImageUtils.getPictureLocation
import oleh.liskovych.gallerygif.utils.MiscellaneousUtils
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import org.jetbrains.anko.support.v4.ctx
import java.io.File

class SignUpFragment : BaseFragment<SignUpViewModel>(), View.OnClickListener {

    companion object {
        private val CURRENT_TEMP_PHOTO_PATH_EXTRA  = MiscellaneousUtils.getExtra("CURRENT_TEMP_PHOTO_PATH", SignUpFragment::class.java)
        private val PHOTO_PATH_EXTRA = MiscellaneousUtils.getExtra("PHOTO_PATH", SignUpFragment::class.java)
        private const val MAX_SIZE_IMAGE = 1024.0 * 1024 * 10
    }

    override val viewModelClass = SignUpViewModel::class.java
    override val layoutId = R.layout.fragment_sign_up

    private val rxPermissions by lazy { RxPermissions(this) }
    private val disposable = CompositeDisposable()
    private var callback: SignUpCallback? = null
    private var currentTempPhotoPath: String? = null
    private var photoPath: String? = null

    private val validationObserver = Observer<Boolean> {
        signUp(it)
    }

    private val signUpSuccessObserver = Observer<Boolean> {
        if (it) navigateToMain()
    }

    private val emailObserver = Observer<ValidationResponse> {
        showValidationError(etEmail, it)
    }

    private val passwordObserver = Observer<ValidationResponse> {
        showValidationError(etPassword, it)
    }

    private val pictureObserver = Observer<ValidationResponse> { response ->
        response.run {
            if (!isValid)
                tvPictureValidationMark.run {
                    show()
                    text = errorMessage
                }
            }
    }

    private val avatarServerErrorObserver = Observer<Boolean> {
        tvPictureValidationMark.run {
            if (it) {
                show()
                text = getString(R.string.profile_picture_is_absent)
            } else hide()
        }
    }

    private val usernameServerError = Observer<String> { raw ->
        raw.takeIf { it.isNotBlank() }?.let { result -> showErrorInField(etUsername, result) }
    }

    private val emailServerError = Observer<String> { raw ->
        raw.takeIf { it.isNotBlank() }?.let { result -> showErrorInField(etEmail, result) }
    }

    private val passwordServerError = Observer<String> { raw ->
        raw.takeIf { it.isNotBlank() }?.let { result -> showErrorInField(etPassword, result) }
    }

    override fun observeLiveData(viewModel: SignUpViewModel) {
        with(viewModel) {
            isValid.observe(this@SignUpFragment, validationObserver)
            isEmailValid.observe(this@SignUpFragment, emailObserver)
            isPasswordsValid.observe(this@SignUpFragment, passwordObserver)
            isPicturePathValid.observe(this@SignUpFragment,pictureObserver)

            avatarError.observe(this@SignUpFragment, avatarServerErrorObserver)
            usernameError.observe(this@SignUpFragment, usernameServerError)
            emailError.observe(this@SignUpFragment, emailServerError)
            passwordError.observe(this@SignUpFragment, passwordServerError)

            isSignUpSuccess.observe(this@SignUpFragment, signUpSuccessObserver)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivProfilePicture, bSend)
        markFieldsRequired(tilEmail, tilPassword)
        savedInstanceState?.let {
            currentTempPhotoPath = it.getString(CURRENT_TEMP_PHOTO_PATH_EXTRA)
            currentTempPhotoPath?.run { capturePictureWithCameraResult() }
            photoPath = it.getString(PHOTO_PATH_EXTRA)
            photoPath?.let { path -> showAvatar(path) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            super.onSaveInstanceState(outState)
            putString(CURRENT_TEMP_PHOTO_PATH_EXTRA, currentTempPhotoPath)
            putString(PHOTO_PATH_EXTRA, photoPath)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivProfilePicture -> fragmentManager?.let {
                tvPictureValidationMark.hide()
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
                    RequestCode.REQUEST_CAPTURE_PICTURE_WITH_CAMERA() -> {
                        capturePictureWithCameraResult()
                    }
                    RequestCode.REQUEST_PICK_PICTURE_FROM_GALLERY() -> data?.data?.let {
                        pickImageFromGalleryResult(it)
                    }
                }
            }
        }
    }

    private fun pickImageFromGalleryResult(uri: Uri) {
        pickedExistingPicture(ctx, uri).path?.let {
            pickImage(it)
        }

    }

    private fun dialogTakePictureResult(type: TakePictureType) {
        when (type) {
            TakePictureType.CAMERA -> capturePictureWithCamera()
            TakePictureType.GALLERY -> pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        disposable.add(rxPermissions
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    context?.let {
                        startActivityForResult(
                            ImageUtils.createImagePickIntentFromGallery(it),
                            RequestCode.REQUEST_PICK_PICTURE_FROM_GALLERY()
                        )
                    }
                }
            }
        )
    }

    private fun capturePictureWithCamera() {
        disposable.add(rxPermissions
            .request(Manifest.permission.CAMERA)
            .subscribe { granted ->
                if (granted) {
                    ImageUtils.createImagePickIntentFromCamera(
                        ctx,
                        {
                            ImageUtils.createImageFileTemp(ctx, false).also { currentTempPhotoPath = it.absolutePath }
                        }, FILE_PROVIDER_NAME
                    )
                        ?.let { startActivityForResult(it, RequestCode.REQUEST_CAPTURE_PICTURE_WITH_CAMERA()) }
                }
            })
    }

    private fun showAvatar(uri: Uri) {
        ivProfilePicture.loadCircularImage(
            uri.toString(),
            R.drawable.shape_avatar_background_progress,
            R.drawable.shape_avatar_background_error
        )
    }

    private fun showAvatar(path: String) {
        ivProfilePicture.loadCircularImage(
            path,
            R.drawable.shape_avatar_background_progress,
            R.drawable.shape_avatar_background_error
        )
    }


    private fun capturePictureWithCameraResult() =
        currentTempPhotoPath?.let { pickImage(it) }

    private fun pickImage(imagePath: String) {
        photoPath = imagePath
        if (imagePath.isNotEmpty()) {
            ImageUtils.run {
                compressImageFromUri(Uri.fromFile(File(imagePath)), maxSize = MAX_SIZE_IMAGE)
                    ?.let {
                        File(imagePath).takeIf { file ->
                            saveBitmap(
                                file,
                                modifyImageToNormalOrientation(it, imagePath)
                            )
                        }
                    }
                    ?.let { imageFile -> showAvatar(Uri.fromFile(imageFile)) }
            }
        }
    }

    private fun signUp(valid: Boolean) {
        if (valid && !photoPath.isNullOrEmpty()) {
            viewModel.sendSignUpRequest(photoPath!!, etUsername.getStringText(), etEmail.getStringText(), etPassword.getStringText())
        }
    }

    private fun showValidationError(textInput: TextInputEditText, response: ValidationResponse) {
        response.run { if (!isValid) showErrorInField(textInput, errorMessage) }
    }

    private fun showErrorInField(textInput: TextInputEditText, error: String?) {
        textInput.error = error
    }

    private fun validateFields() {
        tvPictureValidationMark.hide()
        hideInputFieldsErrors(etUsername, etEmail, etPassword)
        validateUserData()
    }

    private fun validateUserData() = viewModel.validateUserData(
        photoPath, etEmail.getStringText(), etPassword.getStringText()
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