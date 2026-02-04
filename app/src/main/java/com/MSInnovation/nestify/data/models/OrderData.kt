package com.MSInnovation.nestify.data.models

data class OrderData(
    var orderNo: String = "",
    var date: String = "",
    var platform: String = "",
    var cName: String = "",
    var pName: String = "",
    var district: String = "",
    var sellType: String = "",
    var cLocation: String = "",
    var pid: String = "",
    var status: String = "",
    var cPhone: String = "",
    var bPrice: String = "",
    var sPrice: String = "",
    var dCharge: String = "",
    var tCharge: String = "",
    var packaging: String = "",
    var adsCost: String = "",
    var profit: String = "",
    var profitPercent: String = "",
    var docId: String? = null,
    val createdAt: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()

)
