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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.19/authlib.xcframework.zip",// authlib URL
            checksum: "2bd26d98ac3dc2161be1cc1a2925f4b097c0c89bb51643b455830e6cd2a5e3d2"// authlib Checksum
        )
    ]
)