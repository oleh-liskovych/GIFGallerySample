package oleh.liskovych.gallerygif.ui.base.interfaces

interface ProgressNavigationCallback {
    fun hasProgressNavigation(showProgressNavigation: Boolean = false, title: String)
    fun blockBackAction()
}