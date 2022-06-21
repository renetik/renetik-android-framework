# Renetik Android Library
Framework to enjoy, improve and speed up your application development while writing readable code.
This library although used daily is not polished for other people to dive in easily anymore. 
It is used as base library for music production and performance app Renetik Instrument.  
www.renetik.com
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Then you need to create class that extends CSApplication and add it to Manifest.
```kotlin
dependencies {
    implementation 'com.github.renetik.renetik-library-android:renetik-android-framework:1.9.1'
}
```