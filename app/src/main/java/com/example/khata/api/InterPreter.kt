package com.example.khata.api

import com.example.attendence.POJO.DefaultResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InterPreter {

    @FormUrlEncoded
    @POST("/khata/login.php")
    fun login(
        @Field("mobile") mobile: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/khata/addCustomer.php")
    fun addCustomer(
        @Field("name") name: String,
        @Field("rs") rs: String,
        @Field("mobile") mobile: String,
        @Field("token") token: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/khata/getCustomer.php")
    fun getCustomer(
        @Field("token") token: String
    ): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("/khata/getTransaction.php")
    fun getTransaction(
        @Field("customer") id: String
    ): Call<TransactionResponse>

    @FormUrlEncoded
    @POST("/khata/transaction.php")
    fun doTransaction(
        @Field("amount") amount: String,
        @Field("customer") customer: String,
        @Field("note") note: String,
        @Field("mode") mode: String="merchant"
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/khata/updateCustomer.php")
    fun updateCustomer(
        @Field("token") token: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("customer") customer: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/khata/deleteCustomer.php")
    fun deleteCustomer(
        @Field("token") token: String,
        @Field("customer") customer: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/khata/updateUser.php")
    fun updateUser(
        @Field("token") token: String,
        @Field("upi") upi: String
    ): Call<DefaultResponse>

}
