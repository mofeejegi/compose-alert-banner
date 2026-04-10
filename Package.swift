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
            url: "https://github.com/mofeejegi/compose-alert-banner/releases/download/v1.1.0-alpha05/AlertBanner.xcframework.zip",
            checksum: "0eeca4e2fac28090fa31ded09e48926351ef31874d2e77ab29b19cfec83e8cb1"
        )
    ]
)
