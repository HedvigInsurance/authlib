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
            checksum: "01a0c843cbf80ef31396f2abab73c3388329fa2be8457b74be80cdd97362ffce"// authlib Checksum
        )
    ]
)