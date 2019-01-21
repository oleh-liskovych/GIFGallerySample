package oleh.liskovych.gallerygif.ui.screens.main.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.include_empty_list_placeholder.*
import oleh.liskovych.gallerygif.App
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.hide
import oleh.liskovych.gallerygif.extensions.show
import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.ui.base.BaseToolbarFragment
import oleh.liskovych.gallerygif.ui.screens.main.MainViewModel
import oleh.liskovych.gallerygif.ui.screens.main.gallery.adapter.GalleryAdapter
import oleh.liskovych.gallerygif.ui.screens.main.gallery.adapter.GalleryAdapterCallback
import oleh.liskovych.gallerygif.ui.screens.main.gif.GifDialogFragment
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx

class GalleryFragment: BaseToolbarFragment<GalleryViewModel>(),
    GalleryAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    override val viewModelClass = GalleryViewModel::class.java

    override val layoutId = R.layout.fragment_gallery

    private val adapter by lazy { GalleryAdapter(ctx, this) }

    override val toolbarId: Int = R.id.tbToolbar

    override val screenTitleRes: Int = R.string.fragment_gallery_title

    override val showToolbarBackArrow: Boolean = false

    private val activityViewModel: MainViewModel by lazy { ViewModelProviders.of(act).get(MainViewModel::class.java) }

    private val imagesObserver = Observer<List<Image>> {
        adapter.putNewData(it)
    }

    private val gifObserver = Observer<Gif> {
        showGifDialog(it)
    }

    private val emptyListPlaceholder = Observer<Boolean> {
        showPlaceholder(it)
    }

    private val refreshObserver = Observer<Boolean> {
        swipeRefresh.isRefreshing = it
    }

    private val addedItemObserver = Observer<Boolean> {
        if (it) viewModel.loadImages()
        activityViewModel.itemAddedLiveData.postValue(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setupSwipeRefreshLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_gallery, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.actionAdd -> navigateToUpload()
            R.id.actionPlayGif -> viewModel.loadGif()
            R.id.actionExit -> navigateToAuth()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showGifDialog(gif: Gif) {
        fragmentManager?.let {
            GifDialogFragment.newInstance(gif).show(it, GifDialogFragment::class.java.simpleName)
        }
    }

    override fun onRefresh() {
        adapter.run {
            clear()
            notifyDataSetChanged()
        }
        viewModel.loadImages()
    }

    override fun observeLiveData(viewModel: GalleryViewModel) {
        viewModel.run {
            loadImages()
            imagesLiveData.observe(this@GalleryFragment, imagesObserver)
            emptyListPlaceholderLiveData.observe(this@GalleryFragment, emptyListPlaceholder)
            refreshLiveData.observe(this@GalleryFragment, refreshObserver)
            gifLiveData.observe(this@GalleryFragment, gifObserver)
        }
        activityViewModel.itemAddedLiveData.observe(this@GalleryFragment, addedItemObserver)
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefresh.run {
            setOnRefreshListener(this@GalleryFragment)
            setColorSchemeResources(R.color.colorPrimary)
        }
    }

    private fun initRecyclerView() {
        rvGrid.adapter = adapter
    }

    private fun navigateToUpload() {
        findNavController().navigate(R.id.to_upload, null, null)
    }

    private fun navigateToAuth() {
        App.instance.logout()
        findNavController().navigate(R.id.to_auth, null, null)
        activity?.finish()
    }

    override fun onItemClickListener() {
        // do nothing
    }

    private fun showPlaceholder(showPlaceholder: Boolean) {
        clEmptyListPlaceholder.run {
            if (showPlaceholder) show() else hide()
        }
    }
}