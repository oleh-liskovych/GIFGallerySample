package oleh.liskovych.gallerygif.network

import oleh.liskovych.gallerygif.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

object SNetworkModule {

    private val API_ENDPOINT = "${BuildConfig.ENDPOINT}/api/"

    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())

}