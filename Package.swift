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
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/v1.1.0-alpha04/AlertBanner.xcframework.zip",
            checksum: "c64149c87ed69e9d0691943478cd70303722b7664f3c43c306882d75b7f6b884"
        )
    ]
)
