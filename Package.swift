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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.17/authlib.xcframework.zip",// authlib URL
            checksum: "dd489a631c83ec9ecc315c9510b23d55f0bc58a0aa0a04aba1c68f0f6ccc389b"// authlib Checksum
        )
    ]
)