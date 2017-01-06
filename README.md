# Twitone 

## A light Twitter Client for Android

### Software Archeticture

1. Model View Presenter(MVP) - Presenter is used to Abstract the functionality that the individual component of the app provides
2. Repository Pattern - A local and remote data repository are used to get and store data. Provides a way to abstract and encapsulate functionality and caching.

----------------------------------------------------------------------------------------------------

### Libraries used

1.  StorIO
2.  [Dagger 2](http://google.github.io/dagger/)
3.  [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
4.  Twitter4j
5.  [Butterknife](https://github.com/JakeWharton/butterknife)
6.  LeakCanary
7.  Material Progress Bar
8.  Material Drawer
9.  FontAwesome
10. [Glide 3](https://github.com/bumptech/glide)
11. Circular Image view
12. Sub-sampling ImageView
13. [Stetho](http://facebook.github.io/stetho/)
14. AutoValue
15. AutoParcle
16. [Espresso](https://google.github.io/android-testing-support-library/)
17. [Google Play Services](https://developers.google.com/android/guides/overview)


----------------------------------------------------------------------------------------------------


![Phone Screenshot](screenshot/1.png)
![Phone Screenshot](screenshot/2.png)
![Phone Screenshot](screenshot/3.png)
![Phone Screenshot](screenshot/4.png)
![Phone Screenshot](screenshot/5.png)
![Tablet Screenshot](screenshot/6.png)
![Tablet Screenshot](screenshot/7.png)

## License

```
MIT License

Copyright (c) 2017 Aditya Ladwa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```