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
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/VERSION_PLACEHOLDER/AlertBanner.xcframework.zip",
            checksum: "CHECKSUM_PLACEHOLDER"
        )
    ]
)
