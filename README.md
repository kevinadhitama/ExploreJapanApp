# ExploreJapanApp
This app is about helping you exploring Japan!
Through this app you can explore and wander the cities across Japan and also Japanese authentic food recommendation, all is curated for you!

## Feature Implemented
- City List Item
- Dish List Item
- Shimmering Loading
- Swipe to Refresh
- Empty State with refresh
- Error State with refresh
- Portrait and Landscape orientation support
- Local data caching
- Generic and scalable widget

## Technology implemented
- Single Activity Architecture
- Fragment with NavGraph
- ViewModel
- StateFlow
- Coroutines
- Room Dao
- Parameterized Test, Coroutine Test, StateFlow Test

## External Library Used
- Retrofit (for REST API http request)
- Glide (for Image processing)
- JUnit5 (test and annotation libs) & MockK (for Unit Testing)
- Gson (Json converter library)
- Shimmer by facebook

## Running the UT
If you encounter problems when running the Unit Test on MacOS with error something like this :
```
java.lang.ExceptionInInitializerError
Cannot run program ""/Applications/Android Studio.app/Contents/jre/Contents/Home/bin/java""
: error=2, No such file or directory
```
The temporary solution is to rename your Android Studio in `Applications>Android Studio` into `Applications>AndroidStudio`
There's a problem with `ByteBuddy` library that being used in `Mockk` library and [some people also encountering this problem](https://github.com/raphw/byte-buddy/issues/732#issuecomment-858142992)
