package oleh.liskovych.gallerygif.models.converters

interface Converter<IN, OUT> {
    fun convertInToOut(inObject: IN): OUT

    fun convertOutToInt(outObject: OUT): IN
}