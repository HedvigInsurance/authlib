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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/0.0.22/authlib.xcframework.zip",// authlib URL
            checksum: "5862d326943746c0bce9084dc3ef09c976556e9d26952afb1bc41d3ecc56527a"// authlib Checksum
        )
    ]
)