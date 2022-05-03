package com.challenge.meli.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    @SerializedName("query") val name: String,
    @SerializedName("results") val results: List<Product>,
): Parcelable