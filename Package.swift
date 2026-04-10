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
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/v1.1.0-alpha06/AlertBanner.xcframework.zip",
            checksum: "c7a9806533a4ef35b5f708328e91543a8ac61ef2f60e4e71e40c321ad8bd2f53"
        )
    ]
)
