package oleh.liskovych.gallerygif.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun Context.isNetworkConnected() = with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val wifi = getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        wifi.isConnected || mobile.isConnected
    } else {
        var connected = false
        allNetworks
            .asSequence()
            .map { network -> getNetworkInfo(network) }
            .forEach { networkInfo ->
                connected = connected or networkInfo.isConnected
            }
        connected
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

fun View.OnClickListener.setClickListeners(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

fun markFieldsRequired(vararg views: TextInputLayout) {
    views.forEach { it.markRequired() }
}

fun TextInputLayout.markRequired() {
    hint = "$hint *"
}

fun EditText.getStringText(): String {
    return this.text.toString().trim()
}