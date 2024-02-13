// swift-tools-version:5.7
import PackageDescription

let packageName = "authlib"
let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v14),
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        )
    ],
    dependencies: [],
    targets: [
        .binaryTarget(
            name: packageName,
            path: "./authlib/build/XCFrameworks/release/\(packageName).xcframework"
        )
    ]
)