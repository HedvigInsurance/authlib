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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.20/authlib.xcframework.zip",// authlib URL
            checksum: "55db5c48323bc3cf4c63ec42eb17a0178beed17d98a6112d235c9a6b2b438885"// authlib Checksum
        )
    ]
)