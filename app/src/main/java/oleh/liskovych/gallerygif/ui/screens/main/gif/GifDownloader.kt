package oleh.liskovych.gallerygif.ui.screens.main.gif

import kotlinx.coroutines.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels


class GifDownloader(gifFragment: GifDialogFragment) {
    private val fragmentReference = WeakReference(gifFragment)
    private var loadJob: Job? = null

    fun load(gifPath: String) {
        loadJob = GlobalScope.launch {
            try {
                val buffer = downloadGif(gifPath)
                runOnUiThread {
                    onGifDownloaded(buffer)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    onDownloadFailed(e)
                }
            }
        }
    }

    private suspend fun runOnUiThread(action: GifDialogFragment.() -> Unit) {
        withContext(Dispatchers.Main) {
            fragmentReference.get()?.apply {
                action()
            }
        }
    }

    fun destroy() {
        loadJob?.cancel()
    }

    private fun downloadGif(gifPath: String): ByteBuffer {
        val urlConnection = URL(gifPath).openConnection()
        urlConnection.connect()
        val contentLength = urlConnection.contentLength
        if (contentLength < 0) {
            throw IOException("Content-Length header not present")
        }
        urlConnection.getInputStream().use {
            val buffer = ByteBuffer.allocateDirect(contentLength)
            Channels.newChannel(it).use { channel ->
                while (buffer.remaining() > 0) {
                    channel.read(buffer)
                }
                return buffer
            }
        }
    }
}
