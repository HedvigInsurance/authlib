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
            checksum: "6d3b5b1fd15e1af33abaf91caf83568b1b1701c7a1229b67f270c5041b1388cc"// authlib Checksum
        )
    ]
)