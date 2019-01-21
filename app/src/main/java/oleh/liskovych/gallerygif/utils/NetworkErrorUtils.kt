package oleh.liskovych.gallerygif.utils

import io.reactivex.Flowable
import io.reactivex.functions.Function
import oleh.liskovych.gallerygif.EMPTY_STRING
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.error.ErrorTypeOne
import oleh.liskovych.gallerygif.network.error.ErrorTypeTwo
import oleh.liskovych.gallerygif.network.error.validation.ValidationError
import oleh.liskovych.gallerygif.network.error.validation.ValidationErrorChildren
import oleh.liskovych.gallerygif.network.exceptions.ApiException
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

    private const val SERVER_ERROR_NOT_FOUND = 404
    private const val SERVER_ERROR_NOT_ALLOWED = 405

    private val TAG = NetworkErrorUtils::class.java.simpleName

    fun <T> rxParseError() = Function<Throwable, Flowable<T>> {
        Flowable.error<T>(parseError(it))
    }

    private fun parseError(throwable: Throwable): Throwable? {
        return if (throwable is HttpException) {
            val code = throwable.code()
            if (code == SERVER_ERROR_NOT_FOUND || code == SERVER_ERROR_NOT_ALLOWED) {
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
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        try {
            inputStreamReader = InputStreamReader(response.errorBody()?.byteStream())
            bufferedReader = BufferedReader(inputStreamReader)
            val sb = StringBuilder()
            var newLine: String? = null
            while ({newLine = bufferedReader.readLine(); newLine}() != null) {
                sb.append(newLine)
            }

            var errorMessage: String? = null
            var validationError: ValidationErrorChildren? = null
            tryDifferentErrors(sb.toString())?.let {
                when(it) {
                    is ErrorTypeOne -> errorMessage = it.error
                    is ErrorTypeTwo -> errorMessage = it.error?.message
                    is ValidationError -> validationError = it.children
                }
            }
            if (!errorMessage.isNullOrEmpty() || validationError != null) {
                return ApiException(response.code(), errorMessage, validationError)
            }
        } catch (e: IOException) {
            LOG.e(TAG, throwable = e)
        } finally {
            bufferedReader?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    LOG.e(TAG, throwable = e)
                }
            }
            inputStreamReader?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    LOG.e(TAG, throwable = e)
                }
            }
        }
        return ApiException(response.code(), "Error not recognized", null)
    }

    private fun tryDifferentErrors(response: String): Any? {
        val errorTypes = arrayOf(ErrorTypeOne::class.java, ErrorTypeTwo::class.java, ValidationError::class.java)
        var error: Any? = null
        errorTypes.forEach {
            try {
                error = SNetworkModule.mapper.readValue(response, it)
                return@forEach
            } catch (e: IOException) {
                LOG.e(TAG, "Couldn't parse error response to ${it.simpleName}: " + e.message)
            }
        }
        return error
    }

}



















