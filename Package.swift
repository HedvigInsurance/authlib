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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.15/authlib.xcframework.zip",// authlib URL
            checksum: "4621b47847b27a6d926d0dc335478a677f4e9777e5fe3f6527b9207c01d75425"// authlib Checksum
        )
    ]
)