
package com.example.ajubarider


data class Message(
    var message:String
)
data class Order(
        var OID:Int?,
        var contents:String?,
        var price: Int?,
        var date: String?,
        var status:String?,
        var AID:Int?,
        var houseName:String?,
        var streetAddress:String?,
        var latitude:Double?,
        var longitude:Double?,
        var deliveryBoyName:String?,
        var deliveryBoyPhone:String?,


        var phone:String="",


)

data class Address(
        var houseName:String,
        var streetAddress:String,
        var latitude:Double,
        var longitude:Double
)
data class Location(
        var phone:String?,
        var latitude: Double?,
        var longitude: Double?
)
data class Rider(
    var orders:List<Order>?

)