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
            checksum: "b7f2af87c095292b53358e56a8209a60a2369441f94c3af90acd1f22702cdead"// authlib Checksum
        )
    ]
)