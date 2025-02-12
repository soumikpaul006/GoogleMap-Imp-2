package com.codegalaxy.googlemap_impl.model.entity

import com.google.android.gms.maps.model.LatLng

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}