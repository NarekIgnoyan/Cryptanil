package com.primesoft.cryptanil.api

import com.primesoft.cryptanil.models.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*


interface ApiService {

    @POST("createOrder")
    fun createOrder(@Body request: CreateOrder): Observable<Response<OrderInformation>>

    @GET("company/getWalletInfo")
    fun getWalletInformation(@Query("auth") auth: String): Observable<Response<WalletInformation>>

    @GET("company/getCoinAddress")
    fun getCoinAddress(
        @Query("auth") auth: String,
        @Query("coin") coin: String,
        @Query("network") network: String,
    ): Observable<Response<CoinAddress>>

    @POST("company/submitOrder")
    fun submitOrder(@Body request: SubmitOrder): Observable<Response<OrderInformation>>

    @GET("company/getCryptanilOrderInfo")
    fun getOrderInformation(@Query("auth") auth: String): Observable<Response<OrderInformation>>

    @POST("addRefundRequest")
    fun requestRefund(@Body request: RefundRequest): Observable<Response<Any>>

}