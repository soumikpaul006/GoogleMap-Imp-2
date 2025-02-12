# Location-based Android Application

An Android application that demonstrates real-time location tracking with Google Maps integration. The app follows modern Android development practices and architecture patterns.

## Features

- Real-time location tracking
- Google Maps integration
- 5km radius visualization
- Runtime permission handling
- GPS status monitoring
- Clean Architecture with MVVM pattern

## Technical Stack

- **Language**: Kotlin
- **Minimum SDK**: 24
- **Target SDK**: 34

### Architecture & Libraries

- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger-Hilt
- **UI Framework**: Jetpack Compose
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Maps**: Google Maps SDK with Maps Compose
- **Location Services**: Google Play Services Location

## Project Structure

```
app/
├── di/
│   └── AppModule.kt
├── domain/
│   ├── model/
│   │   └── Location.kt
│   └── repository/
│       └── ILocationRepository.kt
├── data/
│   └── repository/
│       └── LocationRepository.kt
└── presentation/
    ├── screen/
    │   └── MapScreen.kt
    ├── state/
    │   └── LocationState.kt
    └── viewmodel/
        └── LocationViewModel.kt
```

## Setup

1. Clone the repository
2. Add your Google Maps API key in `local.properties`:
   ```properties
   MAPS_API_KEY=your_api_key_here
   ```
3. Sync project with Gradle files
4. Run the application

## Features Implementation

### Location Permissions
The app handles runtime permissions for location access:
- Checks for `ACCESS_FINE_LOCATION` permission
- Requests permission if not granted
- Handles permission results appropriately

### GPS Status
Monitors and handles GPS status:
- Checks if GPS is enabled
- Prompts user to enable GPS if disabled
- Automatically updates when GPS status changes

### Location Updates
Implements real-time location tracking:
- Gets current location using FusedLocationProviderClient
- Updates location on map in real-time
- Shows 5km radius circle around current location

### Maps Integration
Uses Google Maps with Compose:
- Displays current location marker
- Shows 5km radius visualization
- Supports zoom controls and location button
- Auto-centers on current location

## State Management

The app uses sealed class `LocationState` to handle different states:
- Initial
- Loading
- RequirePermission
- RequireGps
- Success
- Error

## Architecture Details

### Repository Pattern
- Interface: `ILocationRepository`
- Implementation: `LocationRepository`
- Handles location-related operations

### ViewModel
- Manages UI state using StateFlow
- Handles location updates
- Manages GPS status monitoring

### Dependency Injection
Uses Dagger-Hilt for:
- Repository injection
- FusedLocationProviderClient provision
- Context provision

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Google Play Services
- Active Google Maps API key

