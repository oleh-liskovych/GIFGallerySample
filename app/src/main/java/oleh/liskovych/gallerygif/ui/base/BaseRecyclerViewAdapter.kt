package oleh.liskovych.gallerygif.ui.base

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<TData, TViewHolder : RecyclerView.ViewHolder>(context: Context, data: List<TData> = listOf()) :
        RecyclerView.Adapter<TViewHolder>() {

    protected val context: Context = context.applicationContext
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    val data: MutableList<TData> = data.toMutableList()

    override fun getItemCount() = data.size

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getItem(position: Int): TData = data[position]

    fun updateListItems(newObjects:List<TData>, callback: DiffUtil.Callback) {
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newObjects)
    }

    fun addAll(collection: Collection<TData>) = data.addAll(collection)

    fun clear() {
        data.clear()
    }

}