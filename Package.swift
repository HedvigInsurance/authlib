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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.12/authlib.xcframework.zip",// authlib URL
            checksum: "ec07846629c77c7d48867c564ecbd23ab146c0804409547f61d967050f609da8"// authlib Checksum
        )
    ]
)