<p align="center">
    <a href="https://android-arsenal.com/api?level=34"><img alt="API" src="https://img.shields.io/badge/API-34%2B-brightgreen.svg?style=flat"/></a>
    <a href="https://kotlinlang.org"><img alt="API" src="https://img.shields.io/badge/Kotlin-1.9.23-blue.svg"/></a>
    <a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="API" src="https://img.shields.io/badge/AGP-8.4.0-green?style=flat"/></a>
    <a href="https://codebeat.co/projects/github-com-gabrielgrs1-pokedex-main"><img alt="codebeat badge" src="https://codebeat.co/badges/72a4ee7d-4f44-4d05-9ddc-e396b2fe3131" /></a>
    <a href="https://www.codefactor.io/repository/github/gabrielgrs1/pokedex"><img src="https://www.codefactor.io/repository/github/gabrielgrs1/pokedex/badge" alt="CodeFactor" /></a>
</p>

# Pokedex

A simple pokedex app using the pokeapi.co API

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

## Usage

1. Open the app
2. Click on a pokemon to see more details
3. Click on the heart to favorite a pokemon
4. Click on the back button to go back to the list of pokemon
5. Use the search bar to search for a specific pokemon

## Coverage

// ADD SCREENSHOTS

## Architecture

MVVM (Model-View-ViewModel) architecture is used in this project to separate the concerns of the UI,
business logic, and data using a uni-directional data flow. The architecture is divided into the
following layers:

- Data
- Domain
- Presentation

// ADD SCREENSHOTS

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

// ADD SCREENSHOTS

### Domain

The domain layer contains the business logic of the app. It consists of the following components:

- Use Cases: Defines the actions that can be performed in the app
- Models: Data classes that represent the data
- Mappers: Converts data between the different layers
- Repository interface: Defines the methods that the repository must implement

// ADD SCREENSHOTS

### Presentation

The presentation layer is responsible for displaying the data to the user. It consists of the
following components:

- Screens: Defines the UI of the app
- Components: Reusable UI components
- [View Models](https://developer.android.com/topic/libraries/architecture/viewmodel): Contains the
  logic for the UI
- [UI State](https://developer.android.com/develop/ui/compose/state): Represents the state of the UI
- [UI Navigation](https://developer.android.com/jetpack/compose/navigation): Represents the events
  that can occur in the UI

// ADD SCREENSHOTS

## Libraries Used

### UI

- Jetpack Compose - Modern UI toolkit for building native Android UI
- Jetpack Navigation - Navigation component that helps to implement navigation in the app

### Data

- Retrofit - Type-safe HTTP client for Android and Java
- Room - SQLite object mapping library
- Coil - Image loading library for Android backed by Kotlin Coroutines
- Kotlin Coroutines - Asynchronous programming in Kotlin

### Dependency Injection

- Koin - Dependency injection framework for Kotlin

### Testing

- JUnit - Unit testing framework for Java
- MockK - Mocking library for Kotlin
- Turbine - Testing library for Kotlin Coroutines]

## Screenshots

// ADD SCREENSHOTS

## Future Improvements

- Improve search functionality
- Add UI tests
- Add more details to the pokemon details screen and improve the UI
- Add animations
- Add more features like filtering pokemon by type
- Change DI framework to Hilt
- Implement a modular architecture
- Implement CI/CD pipeline (Unit tests, UI tests, Coverage, Linting, etc.)