// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/HedvigInsurance/authlib/com/hedvig/authlib/authlib-kmmbridge/1.3.16-alpha-20240213160825/authlib-kmmbridge-1.3.16-alpha-20240213160825.zip"
let remoteKotlinChecksum = "796e37d44597864db47c35cab6fb526cd3e633ec026d4361c8aec50033404124"
let packageName = "authlib"
// END KMMBRIDGE BLOCK

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        )
        ,
    ]
)