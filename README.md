# Renetik Android Library

Framework to enjoy, improve and speed up your application development while writing readable code.

[![Build Status](https://travis-ci.org/rene-dohan/renetik-android.svg?branch=master)](https://travis-ci.org/rene-dohan/renetik-android)

# Table of Contents

1. [About](#about)
1. [Getting Started](#getting-started)
2. [Framework](#framework)
2. [Base](#base)
3. [Themes](#themes)
4. [Controller](#controller)
5. [List](#list)
6. [Get Picture](#get-picture)
7. [Maps](#maps)
8. [Dialog](#dialogs)
9. [Json](#json)
10. [Client](#client)
11. [Client OKHTTP3](#client-okhttp3)
12. [Crashlytics](#crashlytics)
13. [Imaging](#imaging)
14. [Material](#material)

# About
I started with this framework few years ago with my first Android application and made similar framework with sisimilareasilymilar classes for iOS too so I could write more easily two applications, one in Java and other in iOS while keeping similar structure of code. Then framework mutated as I was easily kotlindeveloping apps further and 3 months ago I rewrote it to kotlin and polished while removing large part with idea to release it for others and showcase how I code for development clients or companies who can hire me for software development.
I didn't try to re-invent the wheel but with background from multiple platfroms and languages I just see how things can be done nicely without some overcomlexities of Android framework like activities lifetime, fragments, java listeners and so on. For example I missed event classes similar to that used in c# and other programming patterns, like Navigation from iOS so I made them.
I am sure there are number of other libnraries that alrady has those features, with some pros and cons, but I was able to keep this code functional and usefull for me for quite a few years so I dont need to look for code from others that can be quite dificult to fix if problems happens. I always tried to use good libraries but for example CSEvent classes are quite old maybe 7 years now, and are still quite usefull. 

# Getting Started
Good way to start with the framework, is to download checkout or fork it, open in Android Studio and run sample application while look at how code is written. Code is quite readable and self explanatory and in this documentation I will show snippets from sample application and framework to explain how it can be used and how I use it in my own projects.

## Sample Application:
<p align="center">
    <img src="sample/screenshots/Menu Theme 1.png" width="100">
    <img src="sample/screenshots/Maps.png" width="100">
    <img src="sample/screenshots/Simple List.png" width="100">
    <img src="sample/screenshots/Request List Load.png" width="100">
    <img src="sample/screenshots/Log.png" width="100">
    <img src="sample/screenshots/Dynamic Menu Remove.png" width="100">
</p>
Sample application showcase some of the most visible features of framework, some of them are more hidden and I will try to mention most of them. Sample application shows how with few lines of code complex features can be implemented and still keepeng code readable. Basis of application are ViewControllers and NavigationController. Controllers can be pushed to navigation controller stack and in each controller can be used, instead of onCreate,onResume... wery simple methods like onViewShowing with some othe features that makes life easier. But somtimes its even not needed because ListController and simmilar can access them by events delegation. Still underdehood are accessible android methods that are called from Activity if they are needed somehow. But in whole sample application there is no need for them.

I also made sever side project in Spring Boot so Sample application communicate with heroku server by simple rest api. Server sample project is here [Renetik Library Sample Server](https://github.com/rene-dohan/renetik-library-sample-server)

## Framework
[ ![Framework](https://api.bintray.com/packages/rene-dohan/maven/renetik-android:framework/images/download.svg) ](https://bintray.com/rene-dohan/maven/renetik-android:framework/_latestVersion)

The `framework` module contains everything in this library. So it's convenient way to add all to gradle dependencies in one line.

```gradle
dependencies {
  ...
  implementation 'renetik.android:framework:$renetik_version'
}
```
Then you need to create class that extends CSApplication and add it to Manifest.
```gradle
class SampleApplication : CSApplication() {
    override val isDebugBuild = DEBUG
}

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="renetik.android.sample">

    ...
    <application
        android:name=".model.SampleApplication"
        ...
```
Most if not all library classes are prefixed with CS, so they can be easyly recognized and found. 

## Base
[ ![Base](https://api.bintray.com/packages/rene-dohan/maven/renetik-android:base/images/download.svg) ](https://bintray.com/rene-dohan/maven/renetik-android:base/_latestVersion)

The `base` module contains various extensions and classes used across the framework. 

```gradle
dependencies {
  ...
  implementation 'renetik.android:base:$renetik_version'
}
```
Most notable class is CSEvent. It's usage can be understood by looking at this test:
```gradle
    private var eventOneCounter = 0
    private var eventOneValue = ""
    private val eventOne = event<String>()
    @Test
    fun twoListenersCancelBothInSecond() {
        val eventOneRegistration = eventOne.run { _, value ->
            eventOneCounter++
            eventOneValue = value
        }
        eventOne.run { registration, value ->
            eventOneCounter++
            eventOneValue = value
            registration.cancel()
            eventOneRegistration.cancel()
        }
        eventOne.fire("testOne")
        assertEquals(2, eventOneCounter)
        assertEquals("testOne", eventOneValue)

        eventOne.fire("testTwo")
        assertEquals(2, eventOneCounter)
        assertEquals("testOne", eventOneValue)
    }
```
There are also some helper methods that use this class for delegation and decupling, most intresting example of usage is seen in [Maps](#maps) module for automatic cancelation of listeners when view controller is hidden. 


## Themes
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Controller
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## List
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Get Picture
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Json
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Client
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Client OKHTTP3
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Crashlitics
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Imaging
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```

## Material
<p align="center">
    <img src="sample/screenshots/Themes.png" width="100">
    <img src="sample/screenshots/Themes.png" width="100">
</p>

[ ![Core](https://api.bintray.com/packages/drummer-aidan/maven/material-dialogs%3Acore/images/download.svg) ](https://bintray.com/drummer-aidan/maven/material-dialogs%3Acore/_latestVersion)

The `core` module contains everything you need to get started with the library. It contains all
core and normal-use functionality.

```gradle
dependencies {
  ...
  implementation 'com.afollestad.material-dialogs:core:2.0.0-rc7'
}
```
