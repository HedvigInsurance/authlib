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
            url: "https://github.com/HedvigInsurance/authlib/releases/download/v0.0.16/authlib.xcframework.zip",// authlib URL
            checksum: "d28adda15a22c501b893ae89519cdf3e69ad2ea75b999c9e0ff78e3cf06d0652"// authlib Checksum
        )
    ]
)