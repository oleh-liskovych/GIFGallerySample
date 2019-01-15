package oleh.liskovych.gallerygif.models.converters

abstract class BaseConverter<IN, OUT>: Converter<IN, OUT> {

    override fun convertInToOut(inObject: IN): OUT = processConvertedInOut(inObject)

    override fun convertOutToInt(outObject: OUT): IN = processConvertOutToIn(outObject)

    override fun convertListInToOut(inObjects: List<IN>?): List<OUT> =
        inObjects?.map { convertInToOut(it) } ?: listOf()

    override fun convertListOutToIn(outObject: List<OUT>?): List<IN> =
        outObject?.map { convertOutToInt(it) } ?: listOf()

    protected abstract fun processConvertedInOut(inObject: IN): OUT

    protected abstract fun processConvertOutToIn(outObject: OUT): IN
}