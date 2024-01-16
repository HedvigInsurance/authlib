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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/1.3.6/authlib.xcframework.zip",// authlib URL
            checksum: "8ae9b95fd79ed1a5085ebe80c7bc8e7fec4ae80ff1fadc1899758be33922680c"// authlib Checksum
        )
    ]
)