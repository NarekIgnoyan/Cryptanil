package com.primesoft.cryptanil.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.primesoft.cryptanil.BuildConfig
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.enums.Language
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

open class API {

    companion object {
        var BASE_URL = "https://api.cryptanil.com/"
    }

    private val HEADER_LANGUAGE_KEY = "language"

    private val TIME_OUT = 60

    protected fun getGson(): Gson {
        return GsonBuilder()
            .create()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()
    }

    protected fun getHttpClient(): OkHttpClient {
        val builder = getUnsafeOkHttpClient().newBuilder()

        builder.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            requestBuilder.header(
                HEADER_LANGUAGE_KEY,
                app.language ?: Language.EN.getId()
            )

            requestBuilder.method(original.method(), original.body())
            chain.proceed(requestBuilder.build())
        }

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        return builder.build()
    }

}