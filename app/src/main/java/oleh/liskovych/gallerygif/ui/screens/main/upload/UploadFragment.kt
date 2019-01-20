package oleh.liskovych.gallerygif.ui.screens.main.upload

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseToolbarFragment
import org.jetbrains.anko.support.v4.ctx

class UploadFragment: BaseToolbarFragment<UploadViewModel>() {

    override val toolbarId: Int = R.id.tbToolbar

    override val screenTitleRes: Int = R.string.fragment_upload_title

    override val viewModelClass = UploadViewModel::class.java

    override val layoutId = R.layout.fragment_upload

    override val showToolbarBackArrow = true

    override fun observeLiveData(viewModel: UploadViewModel) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_upload, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.actionConfirm -> showToast()
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("")
    private fun showToast() {
        Toast.makeText(ctx, "Send request here", Toast.LENGTH_SHORT).show()
    }

}