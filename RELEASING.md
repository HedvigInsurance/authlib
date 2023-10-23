Releasing
To update the authlib used by Android + iOS and hosted inside GitHub Packages, you'll need to follow the steps below exactly, and the CI workflows for alpha and for release will take care of the rest.

### Release version

1. Go to `main` branch 

2. Update the AUTHLIB_VERSION_NAME in [gradle.properties](/gradle.properties) to the release version (remove the -alpha suffix).

3. Commit

   $ git commit -am "Prepare version X.Y.Z"

4. Tag

   $ git tag -am "Version X.Y.Z" X.Y.Z
   
5. Update AUTHLIB_VERSION_NAME in gradle.properties to the next "alpha" version. e.g. If current is "1.1.1", turn it into "1.1.2-alpha", or "1.2.0-alpha", or "2.0.0-alpha", whichever fits the case.

6. Commit

   $ git commit -am "Prepare next development version"

7. Push!

   $ git push && git push --tags
   This will trigger a GitHub Action workflow which will create a GitHub release and upload the release artifacts to GitHub Packages.