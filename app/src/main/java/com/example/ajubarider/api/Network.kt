package com.example.ajubarider.api

import android.location.Location
import com.example.ajubarider.Message
import com.example.ajubarider.Order
import com.example.ajubarider.Rider

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Network {

    @POST("/Ajuba/test")
    suspend fun test(@Body location: com.example.ajubarider.Location)
    @GET("/Ajuba/admin/getProcessingOrders")
    suspend fun getProcessingOrders(): Response<List<Order>>

    @POST("/Ajuba/rider/{phone}/id/{id}/accepted")
    suspend fun acceptProcessingOrder(@Path("id") id:Int,@Path("phone")phone: Int):Response<Message>

    @GET("/Ajuba/rider/login/{phone}")
    suspend fun getOrders(@Path("phone")id:Int):Response<List<Order>>
    @POST("/Ajuba/rider/login/{phone}/{registrationToken}")
    suspend fun login(@Path("phone")phone:String,@Path("registrationToken")registrationToken:String)
    :Response<Message>
    @POST("/Ajuba/rider/{phone}/location")
    suspend fun updateLocation(@Path("phone")phone:String,@Body location: com.example.ajubarider.Location)
    @POST("/Ajuba/rider/phone/{phone}/id/{id}/delivered")
    suspend fun orderDelivered(@Path("id") id:Int,@Path("phone")phone: Int):Response<Message>
    @POST("/Ajuba/rider/{phone}/{id}/declined")
    suspend fun orderDeclined(@Path("id")id: Int,@Path("phone")phone: Int):Response<Message>
}