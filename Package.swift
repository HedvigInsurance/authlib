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
            checksum: "ab1d3a9ef134283cd404a4d37dd2207739d5ed42575732db0fb2d7b1f9c4e187"// authlib Checksum
        )
    ]
)