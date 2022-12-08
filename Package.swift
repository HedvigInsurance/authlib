// swift-tools-version:5.7
import PackageDescription

let package = Package(
    name: "authlib",
    platforms: [
        .iOS(.v14),
    ],
    products: [
        .library(
            name: "authlib",
            targets: ["authlib"]
        )
    ],
    dependencies: [],
    targets: [
        .binaryTarget(
            name: "authlib",
            url: "https://github.com/HedvigInsurance/authlib/raw/v0.0.9/authlib.xcframework.zip",// authlib URL
            checksum: "7aa610ec42cd4ad6c6d65d0d4a0badce34c71149ea31666ea5fcae330b2173ea"// authlib Checksum
        )
    ]
)