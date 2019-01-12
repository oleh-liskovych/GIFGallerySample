package oleh.liskovych.gallerygif.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadCircularImage(imageUri: String, @DrawableRes placeholder: Int, @DrawableRes error: Int) {
    Glide.with(this.context)
        .load(imageUri)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .error(error)
                .circleCrop())
        .into(this)
}