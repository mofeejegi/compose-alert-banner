// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "AlertBanner",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "AlertBanner",
            targets: ["AlertBanner"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "AlertBanner",
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/v1.1.0-alpha03/AlertBanner.xcframework.zip",
            checksum: "8dfd522fca455f40179f45255ad6b8167645a512b56da10bfba8d54c7e82b09f"
        )
    ]
)
