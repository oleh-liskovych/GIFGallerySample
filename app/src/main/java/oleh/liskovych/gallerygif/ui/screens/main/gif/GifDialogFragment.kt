package oleh.liskovych.gallerygif.ui.screens.main.gif

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_gif.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.ui.base.BaseDialogFragment
import oleh.liskovych.gallerygif.ui.base.FragmentArgumentDelegate
import oleh.liskovych.gallerygif.ui.base.interfaces.BaseView
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow
import pl.droidsonroids.gif.InputSource
import java.io.IOException
import java.nio.ByteBuffer

class GifDialogFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(gif: Gif) =
            GifDialogFragment().apply { this.gif = gif }
    }

    override val layoutId: Int = R.layout.dialog_gif
    override val gravity: Int = Gravity.CENTER_VERTICAL

    override val dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    override val dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT

    private var gif: Gif by FragmentArgumentDelegate()
    private var callback: BaseView? = null
    private val gifDownloader = GifDownloader(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<BaseView>(parentFragment, context)
    }

    override fun onDetach() {
        gifDownloader.destroy()
        callback?.hideProgress()
        callback = null
        super.onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.GifDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGif()
    }

    private fun loadGif() {
        gif.gifPath?.let {
            callback?.showProgress()
            gifDownloader.load(it) }
    }

    fun onGifDownloaded(buffer: ByteBuffer) =
        gifView.setInputSource(InputSource.DirectByteBufferSource(buffer))
            .also { callback?.hideProgress() }

    fun onDownloadFailed(e: IOException) {
        callback?.run {
            hideProgress()
            showSnackBar(view, e.message)
        }
    }


}