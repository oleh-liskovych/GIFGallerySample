package oleh.liskovych.gallerygif.ui.screens.main.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.loadImage
import oleh.liskovych.gallerygif.extensions.setClickListeners
import org.jetbrains.anko.find
import java.lang.ref.WeakReference

interface GalleryAdapterCallback {
    fun onItemClickListener() // pass selected item as an argument
}

interface GalleryHolderCallback {
    fun onClick(position: Int)
}

class GalleryAdapter(context: Context, callback: GalleryAdapterCallback) :
    RecyclerView.Adapter<GalleryAdapter.Holder>(), GalleryHolderCallback{

    private val weakCallback = WeakReference(callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder.newInstance(parent, this)

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    override fun onClick(position: Int) {
        weakCallback.get()?.onItemClickListener()
    }

    class Holder(itemView: View, private val callback: GalleryHolderCallback)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        companion object {
            internal  fun newInstance(parent: ViewGroup?, callback: GalleryHolderCallback) =
                    Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_gallery, parent, false), callback)
        }

        val dummyPics = arrayOf(
            "https://images.unsplash.com/photo-1543145589-0f0cde38b239?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1504&q=80",
            "https://images.unsplash.com/photo-1543136769-44aab962b544?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80",
            "https://images.unsplash.com/photo-1543146992-d27609af3b2f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80",
            "https://images.unsplash.com/photo-1545120494-63d37c41da72?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1051&q=80",
            "https://images.unsplash.com/photo-1545114643-2a7356729134?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=80",
            "https://images.unsplash.com/photo-1545062070-e3918ead6e5a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=925&q=80",
            "https://images.unsplash.com/photo-1545074116-ba9228a4183c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80"
        )

        val dummyDescription = arrayOf(
            "Factory",
            "Christmas",
            "Girl",
            "Another One",
            "Old man",
            "Dishes",
            "Leafs"
        )

        val dummyHashtag = arrayOf(
            "First",
            "Second",
            "Third",
            "Fourth",
            "Fifth",
            "Sixth",
            "Seventh"
        )

        private val set = ConstraintSet()

        private val cvRoot = itemView.find<View>(R.id.cvRoot)
        private val ivPicture = itemView.find<ImageView>(R.id.ivPicture)
        private val tvDescription = itemView.find<TextView>(R.id.tvDescription)
        private val tvHashtag = itemView.find<TextView>(R.id.tvHashtag)

        fun bind() {
            ivPicture.loadImage(dummyPics[adapterPosition], R.drawable.ic_broken_image)
            tvDescription.text = dummyDescription[adapterPosition]
            tvHashtag.text = dummyHashtag[adapterPosition]
            setClickListeners(cvRoot)

//            val ratio = String.format("%d:%d", picture)
        }

        override fun onClick(v: View) {
            when (v.id) {
                R.id.cvRoot -> callback.onClick(adapterPosition)
            }
        }
    }
}