package com.codegalaxy.googlemap_impl

import com.codegalaxy.googlemap_impl.model.entity.Location

sealed class LocationState {
    data object Initial : LocationState()
    data object Loading : LocationState()
    data object RequirePermission : LocationState()
    data object RequireGps : LocationState()
    data class Success(val location: Location) : LocationState()
    data class Error(val message: String) : LocationState()
}