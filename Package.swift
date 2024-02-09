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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/0.0.23/authlib.xcframework.zip",// authlib URL
            checksum: "539b3b332247e89bbd1ef6d6aaf5dee5878910c9395006cc118af3876da24c2e"// authlib Checksum
        )
    ]
)