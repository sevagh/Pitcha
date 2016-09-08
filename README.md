# Pitcha <img src=https://github.com/sevagh/Pitcha/raw/master/app/src/main/res/mipmap-hdpi/ic_launcher.png height="75px">

### Description

This is a pitch detector for Android. It uses the McLeod Pitch Method, adapted from [TarsosDSP](https://github.com/JorenSix/TarsosDSP).

The MPM is implemented in both Java and C (via NDK). This is toggleable in the UI:

<img src="https://github.com/sevagh/Pitcha/raw/master/.github/screenshot.png" width="200px">

#### Benchmarks (DDMS)

Java:

![java](https://github.com/sevagh/Pitcha/raw/master/.github/native-java.png)

NDK:

![ndk](https://github.com/sevagh/Pitcha/raw/master/.github/ndk.png)

#### APK minification

Proguard:

![proguard](https://github.com/sevagh/Pitcha/raw/master/.github/proguard-minified.png)

Proguard + [Redex](https://github.com/facebook/redex):

![redex](https://github.com/sevagh/Pitcha/raw/master/.github/redex.png)

### Install

The repository is an Android Studio project. To install the app, download it from the store links below (outdated), or build from this repo.

There are also apks in this repo:

* [signed apk](https://github.com/sevagh/Pitcha/raw/master/app/app-release.apk)
* [redex unsigned apk](https://github.com/sevagh/Pitcha/raw/master/app/app-redex.apk)
* [redex signed apk](https://github.com/sevagh/Pitcha/raw/master/app/app-redex-signed.apk)

### Store links

* https://play.google.com/store/apps/details?id=com.sevag.pitcha
* http://www.amazon.com/sevagh-Pitcha/dp/B0172GFSDS/ref=sr_1_1?s=mobile-apps&ie=UTF8&qid=1450517577&sr=1-1&keywords=pitcha

I can't update the Play Store version because of the `com.sevag -> io.sevag` refactoring.
