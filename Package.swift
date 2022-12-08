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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.11/authlib.xcframework.zip",// authlib URL
            checksum: "fc9bbf334aa95b12f1a168566089d7f4e7705c105292f512a9597ff7aca7bc8d"// authlib Checksum
        )
    ]
)