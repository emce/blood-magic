#!/bin/sh

# Prints current version for application
COMMITS=$(git rev-list --count HEAD)
COMMIT_COUNT=$((700 + $COMMITS))
section=$(sed -n "/^\[versions\]/,/^\[/p" "./gradle/libs.versions.toml" | sed '$d')
majorVersion=$(echo "$section" | grep "^app " | cut -d "=" -f2- | tr -d ' "')
VERSION="$majorVersion.$COMMIT_COUNT"
echo $VERSION

if [ $# -gt 0 ] ; then
    case $1 in
        android)
          git tag $VERSION-android
          git push origin $VERSION-android
          ;;
        ios)
          git tag $VERSION-ios
          git push origin $VERSION-ios
          ;;
        desktop)
          git tag $VERSION-desktop
          git push origin $VERSION-desktop
          ;;
        all)
          git tag $VERSION-android
          git push origin $VERSION-android
          git tag $VERSION-ios
          git push origin $VERSION-ios
          git tag $VERSION-desktop
          git push origin $VERSION-desktop
          ;;
    esac
fi
