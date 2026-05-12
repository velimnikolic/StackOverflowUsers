# StackOverflow Users

Android app that fetches and displays the top 20 StackOverflow users by reputation.

## Features

- Fetches users from the Stack Exchange API.
- Displays profile image, display name and reputation for each user.
- Supports local follow/unfollow actions without making API calls.
- Persists followed users between app sessions with `SharedPreferences`.
- Shows an empty error state when the API request fails.

## Tech Stack

- Kotlin
- Jetpack Compose
- ViewModel + StateFlow
- Retrofit + Moshi
- Coil
- Hilt
- JUnit + kotlinx-coroutines-test

## Architecture

The project uses a small MVVM-style structure with clear data, domain and UI boundaries:

- `data/remote`: Retrofit API and DTOs for Stack Exchange.
- `data/local`: local follow persistence.
- `data/repository`: combines remote users with local follow status.
- `domain`: app models and repository contract.
- `ui/users`: Compose screen, UI state and ViewModel.

`UsersViewModel` owns screen state and user actions. `UsersRepository` owns data orchestration, so the UI does not depend on Retrofit, DTOs or local storage details. This keeps the follow logic and failure handling straightforward to unit test.

## API

The app calls:

```text
https://api.stackexchange.com/2.3/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow
```

No API key is required for the basic assignment flow.

## Running

Requirements:

- Android Studio with JDK 17
- Android SDK installed

From the project root:

```bash
./gradlew build
```

To install/run, open the project in Android Studio and run the `app` configuration on an emulator or device.

## Tests

Run unit tests with:

```bash
./gradlew test
```

Current unit coverage focuses on:

- DTO to domain mapping.
- Repository success, failure, cancellation and follow persistence behavior.
- ViewModel loading, error state and follow/unfollow state updates.
