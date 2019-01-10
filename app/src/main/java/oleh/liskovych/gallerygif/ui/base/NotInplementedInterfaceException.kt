package oleh.liskovych.gallerygif.ui.base

class NotImplementedInterfaceException(clazz: Class<*>) : ClassCastException(String.format(MESSAGE, clazz)) {

    companion object {
        private val MESSAGE = "You have to implement %s"
    }
}