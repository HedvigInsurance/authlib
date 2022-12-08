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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.10/authlib.xcframework.zip",// authlib URL
            checksum: "a924fc7709ab77e597e02386ea780b0d5d8dafb98b8c3be574dc675523fd6d6d"// authlib Checksum
        )
    ]
)