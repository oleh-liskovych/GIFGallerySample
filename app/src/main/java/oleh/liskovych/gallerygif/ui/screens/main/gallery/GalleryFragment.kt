package oleh.liskovych.gallerygif.ui.screens.main.gallery

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_gallery.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import org.jetbrains.anko.support.v4.ctx

class GalleryFragment: BaseFragment<GalleryViewModel>(), GalleryAdapterCallback {

    override val viewModelClass = GalleryViewModel::class.java

    override val layoutId = R.layout.fragment_gallery

    private val adapter by lazy { GalleryAdapter(ctx, this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvGrid.adapter = adapter
    }

    override fun observeLiveData(viewModel: GalleryViewModel) {
        // do nothing
    }

    override fun onItemClickListener() {
        // do nothing
    }
}