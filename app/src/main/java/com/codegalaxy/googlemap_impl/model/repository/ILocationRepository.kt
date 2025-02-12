package com.codegalaxy.googlemap_impl.model.repository

import com.codegalaxy.googlemap_impl.model.entity.Location
import kotlinx.coroutines.flow.Flow


interface ILocationRepository {

    suspend fun getCurrentLocation(): Result<Location>

    suspend fun checkLocationPermission(): Boolean
    suspend fun isGpsEnabled(): Boolean

    fun observeLocationUpdates(): Flow<Location>
    fun observeGpsStatus(): Flow<Boolean>
}