// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "AlertBanner",
    platforms: [
        .iOS(.v14)
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
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/v1.0.0-rc04/AlertBanner.xcframework.zip",
            checksum: "7d774632c9933cc8e176f3120e73fbb430a1cdfbda7a793c72c30ace4b21c26c"
        )
    ]
)
