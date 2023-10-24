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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v1.3.3/authlib.xcframework.zip",// authlib URL
            checksum: "7a9084bc836162c7035f6844d32bb6d9777304e555b7e6062a6631a3c9cea7ca"// authlib Checksum
        )
    ]
)