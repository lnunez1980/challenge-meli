package com.challenge.meli.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("address") val address: Address?,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("domain_id") val domainId: String,
    @SerializedName("shipping") val shipping: Shipping,
    @SerializedName("attributes") val attributes: List<Attribute>,
    @SerializedName("sold_quantity") val soldQuantity: Int,
    @SerializedName("available_quantity") val availableQuantity: Int,
    @SerializedName("seller_reputation") val sellerReputation: SellerReputation?,
) : Parcelable

@Parcelize
data class Address(
    @SerializedName("state_name") val stateName: String?,
    @SerializedName("city_name") val cityName: String?
) : Parcelable

@Parcelize
data class SellerReputation(
    @SerializedName("power_seller_status") val sellerStatus: String?,
    @SerializedName("transactions") val transactions: Transaction?,
) : Parcelable

@Parcelize
data class Transaction(
    @SerializedName("total") val total: Int,
    @SerializedName("canceled") val canceled: Int,
    @SerializedName("completed") val completed: Int,
    @SerializedName("ratings") val ratings: Rating?,
) : Parcelable

@Parcelize
data class Rating(
    @SerializedName("neutral") val neutral: Double,
    @SerializedName("negative") val negative: Double,
    @SerializedName("positive") val positive: Double
) : Parcelable

@Parcelize
data class Shipping(
    @SerializedName("free_shipping") val freeShipping: Boolean? = false,
    @SerializedName("store_pick_up") val storePickUp: Boolean? = false
) : Parcelable

@Parcelize
data class Attribute(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("value_id") val valueId: String?,
    @SerializedName("value_name") val valueName: String?,
    @SerializedName("attribute_group_id") val attributeGroupId: String?,
    @SerializedName("attribute_group_name") val attributeGroupName: String?
) : Parcelable