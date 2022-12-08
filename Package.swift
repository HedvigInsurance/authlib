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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.14/authlib.xcframework.zip",// authlib URL
            checksum: "ec43a101971373df8685b1818c1476b375d72155f28ce1b7771e49b3ddd3071b"// authlib Checksum
        )
    ]
)