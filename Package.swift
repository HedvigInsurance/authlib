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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.0/authlib.xcframework.zip",// authlib URL
            checksum: "7e1941c0aae7124180297c7580f62c2303701c9331fa19a389599b91cdb05032"// authlib Checksum
        )
    ]
)