package oleh.liskovych.gallerygif.ui.screens.main.upload

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_upload.*
import oleh.liskovych.gallerygif.FILE_PROVIDER_NAME
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.getStringText
import oleh.liskovych.gallerygif.extensions.isNetworkConnected
import oleh.liskovych.gallerygif.extensions.loadImage
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.ui.base.BaseToolbarFragment
import oleh.liskovych.gallerygif.ui.base.RequestCode
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.SignUpFragment
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureDialog
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.dialog.TakePictureType
import oleh.liskovych.gallerygif.ui.screens.main.MainViewModel
import oleh.liskovych.gallerygif.utils.FileUtils
import oleh.liskovych.gallerygif.utils.ImageUtils
import oleh.liskovych.gallerygif.utils.ImageUtils.getPictureLocation
import oleh.liskovych.gallerygif.utils.MiscellaneousUtils
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import java.io.File

class UploadFragment : BaseToolbarFragment<UploadViewModel>(), View.OnClickListener {

    companion object {
        private val CURRENT_TEMP_PHOTO_PATH_EXTRA = MiscellaneousUtils.getExtra("CURRENT_TEMP_PHOTO_PATH", UploadFragment::class.java)
        private val PHOTO_PATH_EXTRA = MiscellaneousUtils.getExtra("PHOTO_PATH", SignUpFragment::class.java)
        private val COORDINATES_META_EXTRA = MiscellaneousUtils.getExtra("COORDINATES_META_EXTRA", SignUpFragment::class.java)
        private val COORDINATES_LISTENER_EXTRA = MiscellaneousUtils.getExtra("COORDINATES_LISTENER_EXTRA", SignUpFragment::class.java)
        private const val MAX_SIZE_IMAGE = 3072.0 * 3072.0 * 10
        private const val MAX_RESOLUTION_IMAGE = 3072
    }

    override val toolbarId: Int = R.id.tbToolbar

    override val screenTitleRes: Int = R.string.fragment_upload_title

    override val viewModelClass = UploadViewModel::class.java

    override val layoutId = R.layout.fragment_upload

    override val showToolbarBackArrow = true

    val activityViewModel: MainViewModel by lazy { ViewModelProviders.of(act).get(MainViewModel::class.java) }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val rxPermissions by lazy { RxPermissions(this) }
    private val disposable = CompositeDisposable()
    private var currentTempPhotoPath: String? = null
    private var photoPath: String? = null
    private var coordinatesMeta: Pair<Float, Float>? = null
    private var coordinatesListener: Pair<Float, Float>? = null
    private var isWaitingForLocation = false

    private val uploadedImageObserver = Observer<Image> {
        view?.let {
            viewModel.hideProgress()
            activityViewModel.itemAddedLiveData.postValue(true)
            findNavController().popBackStack()
        }
    }

    override fun observeLiveData(viewModel: UploadViewModel) {
        viewModel.uploadedImageLiveData.observe(this@UploadFragment, uploadedImageObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivPicture)
        savedInstanceState?.run {
            currentTempPhotoPath = getString(CURRENT_TEMP_PHOTO_PATH_EXTRA)
            currentTempPhotoPath?.run { capturePictureWithCameraResult() }
            photoPath = getString(PHOTO_PATH_EXTRA)
            photoPath?.let { path -> showPicture(path) }
            coordinatesMeta = getSerializable(COORDINATES_META_EXTRA) as? Pair<Float, Float>
            coordinatesListener = getSerializable(COORDINATES_META_EXTRA) as? Pair<Float, Float>
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivPicture -> choosePictureSource()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            super.onSaveInstanceState(outState)
            putString(CURRENT_TEMP_PHOTO_PATH_EXTRA, currentTempPhotoPath)
            putString(PHOTO_PATH_EXTRA, photoPath)
            putSerializable(COORDINATES_META_EXTRA, coordinatesMeta)
            putSerializable(COORDINATES_LISTENER_EXTRA, coordinatesListener)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleApiClient()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_upload, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionConfirm -> gatherInfoAndGo()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        disposable.dispose()
        super.onDetach()
    }

    private fun initGoogleApiClient() {
        if (ctx.isNetworkConnected()) {
            if (fusedLocationProviderClient == null) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(act)
            }
        } else {
            showSnackBar(R.string.no_internet_connection)
        }
    }

    private fun choosePictureSource() {
        fragmentManager?.let {
            TakePictureDialog.newInstance(this, RequestCode.REQUEST_DIALOG_TAKE_PICTURE())
                .show(it, TakePictureDialog::class.java.simpleName)
        }
    }

    private fun dialogTakePictureResult(type: TakePictureType) {
        coordinatesMeta = null
        when (type) {
            TakePictureType.CAMERA -> capturePictureWithCamera()
            TakePictureType.GALLERY -> pickImageFromGallery()
        }
    }

    private fun pickImageFromGalleryResult(uri: Uri) {
        FileUtils.pickedExistingPicture(ctx, uri).path?.let {
            pickImage(it)
        }

    }

    @SuppressLint("MissingPermission")
    private fun askForLocation() {
        disposable.add(rxPermissions
            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe { granted ->
                if (granted) {
                    viewModel.showProgress()
                    fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(act) {
                        viewModel.hideProgress()
                        if (isWaitingForLocation) {
                            isWaitingForLocation = false
                            gatherInfoAndGo()
                        }
                        coordinatesListener = Pair(it.latitude.toFloat(), it.longitude.toFloat())
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

    private fun capturePictureWithCameraResult() =
        currentTempPhotoPath?.let { pickImage(it) }

    private fun pickImage(imagePath: String) {
        coordinatesMeta = getPictureLocation(imagePath)
        photoPath = imagePath
        if (imagePath.isNotEmpty()) {
            ImageUtils.run {

                compressImageFromUri(Uri.fromFile(File(imagePath)), maxResolution = MAX_RESOLUTION_IMAGE, maxSize = MAX_SIZE_IMAGE)
                    ?.let {
                        File(imagePath).takeIf { file ->
                            saveBitmap(
                                file,
                                modifyImageToNormalOrientation(it, imagePath)
                            )
                        }
                    } ?.let { imageFile -> showPicture(Uri.fromFile(imageFile)) }
            }
        }
    }

    private fun showPicture(uri: Uri) {
        ivPicture.loadImage(
            uri.toString(),
            R.drawable.progress_animation,
            R.drawable.ic_broken_image
        )
    }

    private fun showPicture(path: String) {
        ivPicture.loadImage(
            path,
            R.drawable.progress_animation,
            R.drawable.ic_broken_image
        )
    }

    private fun gatherInfoAndGo() {
        photoPath.takeIf { !it.isNullOrBlank() }?.let {
            coordinatesMeta?.run {
                upload(it, first, second)
                return
            }
            coordinatesListener?.run {
                upload(it, first, second)
                return
            }
            isWaitingForLocation = true
            askForLocation()
        }
    }

    private fun upload(path: String, latitude: Float, longitude: Float) {
        viewModel.uploadImage(path, etDescription.getStringText(), etHashtag.getStringText(), latitude, longitude)
    }

}