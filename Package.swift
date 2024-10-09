// swift-tools-version:5.3
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/HedvigInsurance/authlib/com/hedvig/authlib/authlib-kmmbridge/1.4.1-alpha-20241009085400/authlib-kmmbridge-1.4.1-alpha-20241009085400.zip"
let remoteKotlinChecksum = "94611b9c0e60fb7ec0eab429f9331b5d60dd1e61fbfa481e671ab75a1dc6b3ec"
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