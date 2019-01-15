package oleh.liskovych.gallerygif.network

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import oleh.liskovych.gallerygif.BuildConfig
import oleh.liskovych.gallerygif.PreferencesProvider
import oleh.liskovych.gallerygif.network.api.ImageApi
import oleh.liskovych.gallerygif.network.api.UserApi
import oleh.liskovych.gallerygif.network.modules.ImageModuleImpl
import oleh.liskovych.gallerygif.network.modules.UserModuleImpl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object SNetworkModule {

    private const val API_ENDPOINT = BuildConfig.ENDPOINT
    private const val TIMEOUT_IN_SECONDS = 30L
    private const val HEADER_TOKEN_NAME = "token"

    val mapper: ObjectMapper = ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(JodaModule())

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .baseUrl(API_ENDPOINT)
        .client(createHttpClient())
        .build()

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor { chain ->PreferencesProvider
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header(HEADER_TOKEN_NAME, PreferencesProvider.token)
                    .method(original.method(), original.body())
                    .build()
                return@addInterceptor chain.proceed(requestBuilder)
            }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
    }.build()


    fun getUserModule() = UserModuleImpl(retrofit.create(UserApi::class.java))
    fun getImageModule() = ImageModuleImpl(retrofit.create(ImageApi::class.java))

}