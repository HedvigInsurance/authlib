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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.18/authlib.xcframework.zip",// authlib URL
            checksum: "b4d307081080dd637b361d2885b00bf2434c47bf818e6a36f9b7bb551d7cb8b0"// authlib Checksum
        )
    ]
)