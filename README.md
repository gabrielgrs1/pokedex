<p align="center">
    <a href="https://android-arsenal.com/api?level=26"><img alt="API" src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat"/></a>
    <a href="https://kotlinlang.org"><img alt="API" src="https://img.shields.io/badge/Kotlin-1.9.23-blue.svg"/></a>
    <a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="API" src="https://img.shields.io/badge/AGP-8.4.0-green?style=flat"/></a>
    <a href="https://codebeat.co/projects/github-com-gabrielgrs1-pokedex-main"><img alt="codebeat badge" src="https://codebeat.co/badges/72a4ee7d-4f44-4d05-9ddc-e396b2fe3131" /></a>
    <a href="https://www.codefactor.io/repository/github/gabrielgrs1/pokedex"><img src="https://www.codefactor.io/repository/github/gabrielgrs1/pokedex/badge" alt="CodeFactor" /></a>
</p>

# Pokedex

A simple pokedex app using the pokeapi.co API

## Screenshots

![home](https://github.com/gabrielgrs1/pokedex/assets/29669990/00d0c5b8-4c5d-4ffb-adc9-6eee6e6409fb)
![details](https://github.com/gabrielgrs1/pokedex/assets/29669990/51834caa-3d8d-4501-b7d7-eabac8ff6381)
![search](https://github.com/gabrielgrs1/pokedex/assets/29669990/d444e6ec-1085-437d-853a-3aa43a9af8af)
![empty-search](https://github.com/gabrielgrs1/pokedex/assets/29669990/f69e3d27-f0ce-4fb0-8063-fd82111a0953)

## Features

- View a list of pokemon
- View details of a specific pokemon
- Favorite a pokemon
- Search for a specific pokemon
- Offline support (Cached data is used when offline)

## Installation

1. Clone the repository
2. Run `./gradlew build` to build the project
3. Run `./gradlew run` to run the project

**OR**

Download the APK [here](https://github.com/gabrielgrs1/pokedex/releases/tag/v.1.0.0) and install on your phone.

## Usage

1. Open the app
2. Click on a pokemon to see more details
3. Click on the heart to favorite a pokemon
4. Click on the back button to go back to the list of pokemon
5. Use the search bar to search for a specific pokemon

## Coverage

![ViewModelCoverage](https://github.com/gabrielgrs1/pokedex/assets/29669990/5cffff2f-b03f-4996-abdd-e9396237f90b)
![UseCase Coverage](https://github.com/gabrielgrs1/pokedex/assets/29669990/ad6bc36a-6a16-48b1-aabb-9e1bc2522ad9)


## Architecture

MVVM (Model-View-ViewModel) architecture is used in this project to separate the concerns of the UI,
business logic, and data using a uni-directional data flow. The architecture is divided into the
following layers:

- Data
- Domain
- Presentation

![Architeture](https://github.com/gabrielgrs1/pokedex/assets/29669990/4a322b24-02cb-48c4-a4ce-df5c866f3d2e)


### Data

The data layer is responsible for providing data to the app. It consists of the following
components:

- Remote Data Source: Fetches data from the network
- Local Data Source: Fetches data from the local database
- Repository: Acts as a single source of truth for the app. It fetches data from the remote data
  source and saves it to the local data source
- Database: Stores the data locally
- API Service: Defines the API endpoints
- Models: Data classes that represent the data
- Mappers: Converts data between the different layers

![Data Layer ](https://github.com/gabrielgrs1/pokedex/assets/29669990/a00f4d5e-a049-4451-a444-eb2499eb9fa1)


### Domain

The domain layer contains the business logic of the app. It consists of the following components:

- Use Cases: Defines the actions that can be performed in the app
- Models: Data classes that represent the data
- Mappers: Converts data between the different layers
- Repository interface: Defines the methods that the repository must implement

![Domain Layer ](https://github.com/gabrielgrs1/pokedex/assets/29669990/8b43dd7e-8dc9-4ee4-9d43-b1b3584f2cee)

### Presentation

The presentation layer is responsible for displaying the data to the user. It consists of the
following components:

- [Screens](https://developer.android.com/develop/ui/compose): Defines the UI of the app
- [Components](https://developer.android.com/develop/ui/compose/components): Reusable UI components
- [View Models](https://developer.android.com/topic/libraries/architecture/viewmodel): Contains the
  logic for the UI
- [UI State](https://developer.android.com/develop/ui/compose/state): Represents the state of the UI
- [UI Navigation](https://developer.android.com/jetpack/compose/navigation): Represents the events
  that can occur in the UI

![Presentation Layer ](https://github.com/gabrielgrs1/pokedex/assets/29669990/c7b8ab11-6707-452b-ba18-38df7e1b8d67)


##  Used Libraries

### UI

- [Jetpack Compose](https://developer.android.com/develop/ui/compose) - Modern UI toolkit for building native Android UI
- [Jetpack Navigation](https://developer.android.com/guide/navigation) - Navigation component that helps to implement navigation in the app

### Data

- [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - SQLite object mapping library
- [Coil](https://coil-kt.github.io/coil/compose/) - Image loading library for Android backed by Kotlin Coroutines
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html#tutorials) - Asynchronous programming in Kotlin

### Dependency Injection

- Koin - Dependency injection framework for Kotlin 

### Testing

- [JUnit](https://developer.android.com/training/testing/local-tests) - Unit testing framework for Java
- [MockK](https://mockk.io/) - Mocking library for Kotlin
- [Turbine](https://github.com/cashapp/turbine) - Testing library for Kotlin Coroutines

## Future Improvements

- Implement CI/CD pipeline (Unit tests, UI tests, Coverage, Linting, etc.)
- Improve search functionality
- Add UI tests
- Add more details to the pokemon details screen and improve the UI
- Add animations
- Add more features like filtering pokemon by type
- Change DI framework to Hilt
- Implement a modular architecture
