# Around Egypt - Android App

## 🎥 App Demonstration


https://github.com/user-attachments/assets/761b38fd-f338-4df3-84ee-edabcad30d8a




## 📌 Overview

**Around Egypt** is an Android app that lets users explore various experiences across Egypt. The app displays recent and recommended experiences on the home screen, where users can like experiences and search for specific ones. When a user likes an experience, the like count updates automatically, and views are also tracked. Clicking on an experience opens the detail screen, showing full details and allowing users to like it from there as well.

---

## 🚀 Technologies Used

- **Kotlin** - Main programming language.
- **MVVM Architecture** - Ensures better code organization and maintainability.
- **Jetpack Compose** - Used for building the UI.
- **Retrofit** - Handles API requests.
- **Room Database** - Stores data locally.
- **Kotlin Coroutines** - Manages background tasks.
- **StateFlow & SharedFlow** - Handles reactive data updates.
- **Unit Testing & Instrumentation Testing** - Ensures app reliability.
- **MockK** - Used for mocking dependencies in unit tests.
- **Turbine** - Used for testing Kotlin Flows.

---

## 📂 Project Structure

The project is structured into modules for better organization:

- **core**: Contains shared components like networking, database, repositories, mappers, and dependency injection setup.
- **home**: Handles the home screen, where users can see recent and recommended experiences, like them, and search. It includes `HomeScreen`, `HomeViewModel`, `SearchExperienceUseCase`, and dependency injection setup.
- **detail**: Manages the detail screen, displaying full experience details and allowing users to like them. It includes `DetailScreen`, `DetailViewModel`, and dependency injection setup.

This modular structure makes the app scalable and easy to maintain.

---

✅ Testing
The app includes unit and instrumentation tests to verify core functionalities, such as fetching experiences, liking experiences, repository interactions, and search functionality. MockK is used for mocking dependencies, and Turbine is used for Flow assertions, ensuring reliable and predictable results.

