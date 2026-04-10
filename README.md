# Compose Alert Banner

[![Kotlin](https://img.shields.io/badge/kotlin-2.1.20-blue.svg?logo=kotlin)](https://kotlinlang.org) 
[![Compose](https://img.shields.io/badge/compose-1.8.2-blue.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform) 
[![Author](https://img.shields.io/badge/author-mofeejegi-gray.svg?logo=github)](https://github.com/mofeejegi) 
[![Apache-2.0](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0) 
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24) 
[![Maven Central](https://img.shields.io/maven-central/v/com.mofeejegi.alert/alert-banner-compose/1.1.0-alpha06)](https://central.sonatype.com/artifact/com.mofeejegi.alert/alert-banner-compose/1.1.0-alpha06)

A **simple**, **customizable**, and **modern** library for displaying alert banners in your Jetpack Compose, Compose Multiplatform, and native iOS (Swift) applications. Easily integrate and adapt to suit any style or use case—from error notifications to informational messages!

<img src="docs/readme_images/compose%20banner.png" alt="Banner">

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Samples](#samples)
- [Installation](#installation)
- [Compose Usage](#compose-usage)
- [iOS (Swift) Usage](#ios-swift-usage)
- [Configuration & Customization](#configuration--customization)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

**Compose Alert Banner** is a Kotlin library powered by Compose Multiplatform, dedicated to providing developers an easy and elegant way to add alert banners to their applications. This library comes with a robust set of features, allowing your alert banners to be as simple or as sophisticated as needed.

Whether you need to show warnings, confirmations, or general notifications, Compose Alert Banner provides an intuitive API and customizable components that blend seamlessly with your app’s UI.

## Features

- **Modular Design:** Easily integrate into your existing Compose projects.
- **Customizability:** Fully customizable UI components with support for custom icons, colors, and typography.
- **Simplicity:** Minimal setup required—get started with just a few lines of code.
- **Animation Ready:** Animate your banners for a more engaging user experience.
- **Lightweight:** Minimal impact on your build size and runtime performance.

## Samples

Here's how it looks:

<table>
  <thead>
    <tr>
      <th>Android</th>
      <th>iOS</th>
      <th colspan="2">Desktop</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><img src="docs/readme_images/alert_android.gif" width="250" alt="Android Sample"/></td>
      <td><img src="docs/readme_images/alert_ios.gif" width="250" alt="iOS Sample"/></td>
      <td colspan="2"><img src="docs/readme_images/alert_desktop.gif" width="500" alt="Desktop Sample"/></td>
    </tr>
  </tbody>
</table>

## Installation

[![Maven Central](https://img.shields.io/maven-central/v/com.mofeejegi.alert/alert-banner-compose/1.1.0-alpha06)](https://central.sonatype.com/artifact/com.mofeejegi.alert/alert-banner-compose/1.1.0-alpha06)

Compose Alert Banner is available as a Gradle dependency on `mavenCentral`. You can add it to your project by including the following in your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mofeejegi.alert:alert-banner-compose:<latest-version>")
}
```
Replace `<latest-version>` with the current release version available in the [Releases](https://github.com/mofeejegi/compose-alert-banner/releases) section.

## Compose Usage

Integrating the alert banner into your Compose application is straightforward. Below is a simple example to get you started:

```kotlin
@Composable
fun MyApp() {
    MaterialTheme {
        AlertBanner(darkTheme = false) { // Declare this at the root of your application

            // This is accessible at any part of your application
            val alertManager = rememberAlertManager()

            Column(modifier = Modifier.fillMaxSize()) {
                Button(onClick = { alertManager.show("Error", AlertBannerType.Error) }) {
                    Text("Show Error Alert")
                }

                Button(onClick = { alertManager.show("Success", AlertBannerType.Success) }) {
                    Text("Show Success Alert")
                }
            }
        }
    }
}
```

## iOS (Swift) Usage

Compose Alert Banner can be used directly in native Swift iOS projects via Swift Package Manager — no Compose or Kotlin knowledge required.

### Installation (Swift Package Manager)

In Xcode, go to **File > Add Package Dependencies** and enter:

```
https://github.com/mofeejegi/compose-alert-banner
```

Select the latest version from the [Releases](https://github.com/mofeejegi/compose-alert-banner/releases) page.

### SwiftUI / UIKit (Window Overlay)

Call `attach()` once at the root of your app. Then call `showSuccess` or `showError` from anywhere.

```swift
import AlertBanner

@main
struct MyApp: App {
    let alertBanner = AlertBannerIOS.shared

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear { alertBanner.attach() }
        }
    }
}

struct ContentView: View {
    let alertBanner = AlertBannerIOS.shared

    var body: some View {
        VStack {
            Button("Show Success") {
                alertBanner.showSuccess(message: "Saved successfully!")
            }
            Button("Show Error") {
                alertBanner.showError(message: "Something went wrong")
            }
        }
    }
}
```

### UIKit (Child View Controller)

Call `install(on:)` once on your root view controller. Then call `showSuccess` or `showError` from anywhere.

```swift
import AlertBanner

class RootViewController: UIViewController {
    let alertBanner = AlertBannerIOS.shared

    override func viewDidLoad() {
        super.viewDidLoad()
        alertBanner.install(on: self)
    }

    @objc func onSave() {
        alertBanner.showSuccess(message: "Saved successfully!")
    }

    @objc func onError() {
        alertBanner.showError(message: "Something went wrong")
    }
}
```

### API Reference

| Method                   | Description                                                                                    |
|--------------------------|------------------------------------------------------------------------------------------------|
| `AlertBannerIOS.shared`  | Singleton instance - use from anywhere                                                         |
| `.attach()`              | Creates a window overlay. Call once at the root                                                |
| `.install(on:)`          | Adds as a child view controller. Call once on the root view controller                         |
| `.showSuccess(message:)` | Shows a success alert banner                                                                   |
| `.showError(message:)`   | Shows an error alert banner                                                                    |
| `.configure(darkTheme:)` | Sets dark/light theme. Call before `attach()` or `install(on:)`. Defaults to system appearance |
| `.detach()`              | Removes the overlay window                                                                     |
| `.uninstall()`           | Removes the child view controller                                                              |

## Configuration & Customization

Compose Alert Banner offers multiple configuration options:

- **Theme Customization:** Override default color schemes and typography.
- **Icon Support:** Use any [Material Icon](https://fonts.google.com/icons) or your own custom vector to match your design.
- **Animation:** Configure entrance and exit animations for the banner.
- **Callbacks:** Implement custom click or dismiss callbacks to run your own business logic.

## Contributing

Contributions, issues, and feature requests are welcome! Please:

1. **Fork** the repository.
2. **Create a branch** (`git checkout -b feature/YourFeature`).
3. **Commit your changes** (`git commit -am 'Add new feature'`).
4. **Push to the branch** (`git push origin feature/YourFeature`).
5. **Open a pull request** describing your changes.

Be sure to follow the existing code style and include tests where applicable.

## License

This project is licensed under the [Apache License 2.0](./LICENSE). See the [LICENSE](./LICENSE) file for details.

## Contact

For questions or suggestions, open an issue or reach out directly:

- **Maintainer:** [mofeejegi](https://github.com/mofeejegi)
