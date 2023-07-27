package com.primesoft.cryptanil.api


import com.primesoft.cryptanil.models.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiImplementation : API(), ApiService {

    override fun createOrder(request: CreateOrder): Observable<Response<OrderInformation>> =
        apiService.createOrder(request)

    override fun getWalletInformation(auth: String): Observable<Response<WalletInformation>> =
        apiService.getWalletInformation(auth)

    override fun getCoinAddress(
        auth: String,
        coin: String,
        network: String
    ): Observable<Response<CoinAddress>> =
        apiService.getCoinAddress(auth, coin, network)

    override fun getOrderInformation(auth: String): Observable<Response<OrderInformation>> =
        apiService.getOrderInformation(auth)

    override fun requestRefund(request: RefundRequest): Observable<Response<Any>> =
        apiService.requestRefund(request)

    override fun submitOrder(request: SubmitOrder): Observable<Response<OrderInformation>> =
        apiService.submitOrder(request)

    private var apiService: ApiService

    private object Holder {
        val INSTANCE = ApiImplementation()
    }

    companion object {
        val instance: ApiImplementation by lazy { Holder.INSTANCE }
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getHttpClient())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

}
