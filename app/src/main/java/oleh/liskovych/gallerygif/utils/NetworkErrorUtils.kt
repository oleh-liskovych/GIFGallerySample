package oleh.liskovych.gallerygif.utils

import io.reactivex.Flowable
import io.reactivex.functions.Function
import oleh.liskovych.gallerygif.network.exceptions.NoNetworkException
import oleh.liskovych.gallerygif.network.exceptions.ServerException
import retrofit2.HttpException
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.UnknownHostException

object NetworkErrorUtils {

    private const val NO_STATUS_CODE = -1
    private const val SERVER_ERROR_BAD_REQUEST = 400
    private const val SERVER_ERROR_NOT_FOUND = 404
    private const val SERVER_ERROR_NOT_ALLOWED = 405

    private val TAG = NetworkErrorUtils::class.java.simpleName

    fun <T> rxParseError() = Function<Throwable, Flowable<T>> {
        Flowable.error<T>(parseError(it))
    }

    private fun parseError(throwable: Throwable): Throwable? {
        return if (throwable is HttpException) {
            val code = throwable.code()
            if (code == SERVER_ERROR_NOT_FOUND || code == SERVER_ERROR_NOT_ALLOWED || code == SERVER_ERROR_BAD_REQUEST) {
                LOG.e(TAG, throwable = throwable)
                return ServerException().initCause(throwable)
            }
            return parseErrorResponseBody(throwable.response())
        } else when {
            isConnectionProblem(throwable) -> NoNetworkException()
            else -> throwable
        }
    }

    private fun isConnectionProblem(throwable: Throwable) =
        throwable is UnknownHostException || throwable is ConnectException

    private fun parseErrorResponseBody(response: Response<*>): Exception {
        val inputStreamReader: InputStreamReader?
        val bufferedReader: BufferedReader?
        try {
            inputStreamReader = InputStreamReader(response.errorBody()?.byteStream())
            bufferedReader = BufferedReader(inputStreamReader)
            val sb = StringBuilder()
            var newLine: String? = null
            while ({newLine = bufferedReader.readLine(); newLine}() != null) {
                sb.append(newLine)
            }

            // todo: parse errors


        } catch (e: IOException) {
            LOG.e(TAG, throwable = e)
        }



        throw Exception() // todo: remove it
    }

}



















