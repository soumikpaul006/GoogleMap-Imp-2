package com.codegalaxy.googlemap_impl.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codegalaxy.googlemap_impl.LocationState
import com.codegalaxy.googlemap_impl.model.repository.ILocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: ILocationRepository
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationState>(LocationState.Initial)
    val locationState = _locationState.asStateFlow()


    init {
        viewModelScope.launch {
            locationRepository.observeGpsStatus()
                .collect { isEnabled ->
                    if (isEnabled) {
                        checkAndRequestLocation()
                    } else {
                        // Explicitly set RequireGps state when GPS is disabled
                        _locationState.value = LocationState.RequireGps
                    }
                }
        }
    }


    fun checkAndRequestLocation() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading

            try {
                if (!locationRepository.checkLocationPermission()) {
                    _locationState.value = LocationState.RequirePermission
                    return@launch
                }

                if (!locationRepository.isGpsEnabled()) {
                    _locationState.value = LocationState.RequireGps
                    return@launch
                }

                locationRepository.getCurrentLocation()
                    .onSuccess { location ->
                        _locationState.value = LocationState.Success(location)
                    }
                    .onFailure { error ->
                        _locationState.value = LocationState.Error(error.message ?: "Unknown error")
                    }

            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Unknown error")
            }
        }
    }

//    fun startLocationUpdates() {
//        viewModelScope.launch {
//            locationRepository.observeLocationUpdates()
//                .collect { location ->
//                    _locationState.value = LocationState.Success(location)
//                }
//        }
//    }

}
