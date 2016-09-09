# Pitcha <img src=app/src/main/res/mipmap-hdpi/ic_launcher.png height="30px">

[![playstore](.static/play.png)](https://play.google.com/store/apps/details?id=com.sevag.pitcha) [![amzn](.static/amazon.png)](http://www.amazon.com/sevagh-Pitcha/dp/B0172GFSDS/ref=sr_1_1?s=mobile-apps&ie=UTF8&qid=1450517577&sr=1-1&keywords=pitcha)


### Description

This is a pitch detector for Android. It uses the McLeod Pitch Method, adapted from [TarsosDSP](https://github.com/JorenSix/TarsosDSP).

The MPM is implemented in both Java and C (via NDK). This is toggleable in the UI:

<img src=".static/screenshot.png" width="200px">

#### Benchmarks (DDMS)

Java:

![java](.static/native-java.png)

NDK:

![ndk](.static/ndk.png)

#### APK minification

Proguard:

![proguard](.static/proguard-minified.png)

Proguard + [Redex](https://github.com/facebook/redex):

![redex](.static/redex.png)

### Install

The repository is an Android Studio project. To install the app, download it from the store links (outdated code), or build from this repo.

There are also apks in this repo:

* [signed apk](app/app-release.apk)
* [redex unsigned apk](app/app-redex.apk)
* [redex signed apk](app/app-redex-signed.apk)
