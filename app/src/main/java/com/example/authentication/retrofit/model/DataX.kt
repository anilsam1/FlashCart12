package com.example.authentication.retrofit.model

data class DataX(
    val __v: Int?,
    val _id: String?,
    val cartItemId: String?,
    val description: String?,
    val imageUrl: String?,
    val price: String?,
    val updatedAt: String?,
    val quantity: Int?,
    val title: String?,
    val watchListItemId: String?,
    val createdAt: String?,
    val productId: String?,
    val userId: String?,
    val productDetails: ProductDetails?,
    val cartId: String?,
    val itemTotal: Float?,


)

{
    var watchListId:String?=null
    var isWishlisted:Boolean?=false
    init {
        isWishlisted = watchListItemId != null

        if(watchListItemId!=null){
            watchListId=watchListItemId
        }
    }

}