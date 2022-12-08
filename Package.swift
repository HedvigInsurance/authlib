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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.13/authlib.xcframework.zip",// authlib URL
            checksum: "fe163051016c08915c778556722dc438a170cc05448e6179ad6aad27d038a4e5"// authlib Checksum
        )
    ]
)