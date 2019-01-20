package oleh.liskovych.gallerygif.ui.screens.main.gallery.adapter

import androidx.recyclerview.widget.DiffUtil
import oleh.liskovych.gallerygif.models.Image

class GalleryDiffCallback(private val oldList: List<Image>, private val newList: List<Image>): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}