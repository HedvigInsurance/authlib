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
            checksum: "1cdb7767416958a99ad2f92b889ef1e2fb399cbaff477579a38fc68bdec20dcb"// authlib Checksum
        )
    ]
)