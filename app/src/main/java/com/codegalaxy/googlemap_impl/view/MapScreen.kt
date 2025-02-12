
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codegalaxy.googlemap_impl.LocationState
import com.codegalaxy.googlemap_impl.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.provider.Settings
import android.Manifest

@Composable
fun MapScreen(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationState by viewModel.locationState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.checkAndRequestLocation()
        }
    }

    // GPS Settings launcher
    val gpsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Recheck location when returning from GPS settings
        viewModel.checkAndRequestLocation()
    }

    LaunchedEffect(Unit) {
        viewModel.checkAndRequestLocation()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = locationState) {
            is LocationState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LocationState.RequirePermission -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Location permission is required")
                    Button(
                        onClick = {
                            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Grant Permission")
                    }
                }
            }

            is LocationState.RequireGps -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("GPS is required")
                    Button(
                        onClick = {
//                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            gpsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Enable GPS")
                    }
                }
            }

            is LocationState.Success -> {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        state.location.toLatLng(),
                        15f
                    )
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = true
                    )
                ) {
                    // Current location marker
                    Marker(
                        state = MarkerState(position = state.location.toLatLng()),
                        title = "Current Location"
                    )

                    // 5km radius circle
                    Circle(
                        center = state.location.toLatLng(),
                        radius = 5000.0, // 5km in meters
                        fillColor = Color(0x220000FF),
                        strokeColor = Color.Blue,
                        strokeWidth = 2f
                    )
                }
            }

            is LocationState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = { viewModel.checkAndRequestLocation() },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> Unit
        }
    }
}