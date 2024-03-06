// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/HedvigInsurance/authlib/com/hedvig/authlib/authlib-kmmbridge/1.3.21-alpha-20240306171338/authlib-kmmbridge-1.3.21-alpha-20240306171338.zip"
let remoteKotlinChecksum = "4b4c999638de52ed35e5d4dc94dae940feb92afdfc248bfc9899ee7be1cfae65"
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