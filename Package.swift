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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.21/authlib.xcframework.zip",// authlib URL
            checksum: "c7cee0975bd9b13f36a853d5acf0b5a4a9ac546e0da0a42d7b92eff0ec6d5168"// authlib Checksum
        )
    ]
)