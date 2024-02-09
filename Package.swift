// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/HedvigInsurance/authlib/com/hedvig/authlib/authlib-kmmbridge/1.3.15-alpha-20240209122920/authlib-kmmbridge-1.3.15-alpha-20240209122920.zip"
let remoteKotlinChecksum = "c75f2fe607ad7f735c1620495a7bcdb152b511db662fcc17734e510429423f79"
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