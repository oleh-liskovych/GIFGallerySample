package oleh.liskovych.gallerygif.ui.base

enum class RequestCode(private val requestCode: Int) {
    REQUEST_DIALOG_TAKE_PICTURE(0),
    REQUEST_CAPTURE_PICTURE_WITH_CAMERA(1),
    REQUEST_PICK_PICTURE_FROM_GALLERY(2);

    operator fun invoke() = requestCode
}