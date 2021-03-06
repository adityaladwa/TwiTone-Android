# Twitone [![GitHub stars](https://img.shields.io/github/stars/LadwaAditya/TwiTone-Android.svg)](https://github.com/LadwaAditya/TwiTone-Android/stargazers) [![GitHub forks](https://img.shields.io/github/forks/LadwaAditya/TwiTone-Android.svg)](https://github.com/LadwaAditya/TwiTone-Android/network) [![GitHub issues](https://img.shields.io/github/issues/LadwaAditya/TwiTone-Android.svg)](https://github.com/LadwaAditya/TwiTone-Android/issues)


## A light Twitter Client for Android

### Software Archeticture

1. Model View Presenter(MVP) - Presenter is used to Abstract the functionality that the individual component of the app provides
2. Repository Pattern - A local and remote data repository are used to get and store data. Provides a way to abstract and encapsulate functionality and caching.

----------------------------------------------------------------------------------------------------




### Show some :heart: and star the repo to support the project
[![GitHub stars](https://img.shields.io/github/stars/LadwaAditya/TwiTone-Android.svg?style=social&label=Star)](https://github.com/LadwaAditya/TwiTone-Android) [![GitHub forks](https://img.shields.io/github/forks/LadwaAditya/TwiTone-Android.svg?style=social&label=Fork)](https://github.com/LadwaAditya/TwiTone-Android/fork) [![GitHub watchers](https://img.shields.io/github/watchers/LadwaAditya/TwiTone-Android.svg?style=social&label=Watch)](https://github.com/LadwaAditya/TwiTone-Android) [![GitHub followers](https://img.shields.io/github/followers/LadwaAditya.svg?style=social&label=Follow)](https://github.com/LadwaAditya)
[![Twitter Follow](https://img.shields.io/twitter/follow/LadwaAditya.svg?style=social)](https://twitter.com/adi_ladwa)

## Libraries used

1.  [StorIO](https://github.com/pushtorefresh/storio)
2.  [Dagger 2](http://google.github.io/dagger/)
3.  [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
4.  [Twitter4j](http://twitter4j.org/en/)
5.  [Butterknife](https://github.com/JakeWharton/butterknife)
6.  [LeakCanary](https://github.com/square/leakcanary)
7.  [Material Progress Bar](https://github.com/DreaminginCodeZH/MaterialProgressBar)
8.  [Material Drawer](https://github.com/mikepenz/MaterialDrawer)
9.  [FontAwesome](https://github.com/FortAwesome/Font-Awesome)
10. [Glide 3](https://github.com/bumptech/glide)
11. [Circular Image view](https://github.com/hdodenhof/CircleImageView)
12. [Sub-sampling ImageView](https://github.com/davemorrissey/subsampling-scale-image-view)
13. [Stetho](http://facebook.github.io/stetho/)
14. [AutoValue](https://github.com/google/auto/tree/master/value)
15. [AutoParcle](https://github.com/frankiesardo/auto-parcel)
16. [Espresso](https://google.github.io/android-testing-support-library/)
17. [Google Play Services](https://developers.google.com/android/guides/overview)


----------------------------------------------------------------------------------------------------

## Buiding

To build, install and run a debug version, run this from the root of the project:

```
./gradlew app:assembleDebug
```


![Phone Screenshot](screenshot/1.png)
![Phone Screenshot](screenshot/2.png)
![Phone Screenshot](screenshot/3.png)
![Phone Screenshot](screenshot/4.png)
![Phone Screenshot](screenshot/5.png)
![Tablet Screenshot](screenshot/6.png)
![Tablet Screenshot](screenshot/7.png)

## License

```
Copyright 2017 Aditya Ladwa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```