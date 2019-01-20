package oleh.liskovych.gallerygif.ui.screens.main.gallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.hide
import oleh.liskovych.gallerygif.extensions.loadImage
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.extensions.show
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.ui.base.BaseRecyclerViewAdapter
import org.jetbrains.anko.find
import java.lang.ref.WeakReference

interface GalleryAdapterCallback {
    fun onItemClickListener() // pass selected item as an argument
}

interface GalleryHolderCallback {
    fun onClick(position: Int)
}

class GalleryAdapter(context: Context, callback: GalleryAdapterCallback) : BaseRecyclerViewAdapter<Image, GalleryAdapter.Holder>(context),
    GalleryHolderCallback {

    private val weakCallback = WeakReference(callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder.newInstance(
            parent,
            this
        )

    fun putNewData(newItems: List<Image>) {
        updateListItems(newItems, GalleryDiffCallback(data, newItems))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
         getItem(position).let { holder.bind(it) }
    }

    override fun onClick(position: Int) {
        weakCallback.get()?.onItemClickListener()
    }

    class Holder(itemView: View, private val callback: GalleryHolderCallback)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        companion object {
            internal  fun newInstance(parent: ViewGroup?, callback: GalleryHolderCallback) =
                Holder(
                    LayoutInflater.from(
                        parent?.context
                    ).inflate(R.layout.item_gallery, parent, false), callback
                )
        }

        private val cvRoot = itemView.find<View>(R.id.cvRoot)
        private val ivPicture = itemView.find<ImageView>(R.id.ivPicture)
        private val tvWeather = itemView.find<TextView>(R.id.tvWeather)
        private val tvAddress = itemView.find<TextView>(R.id.tvAddress)

        fun bind(item: Image) {
            item.smallImagePath?.let { ivPicture.loadImage(it, R.drawable.progress_animation, R.drawable.ic_broken_image) }

            tvWeather.run {
                item.parameters?.weather?.let {
                    if (it.isNotBlank()) {
                        show()
                        text = it
                    } else hide()
                } ?: hide()
            }

            tvAddress.run {
               item.parameters?.address?.let {
                   if (it.isNotBlank()) {
                       show()
                       text = it
                   } else hide()
               } ?: hide()
            }
            setClickListeners(cvRoot)

        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.cvRoot -> callback.onClick(adapterPosition)
            }
        }
    }
}