## Steps to Add C++ Code to Your Jetpack compose project with groovy DSL

1. **Set Up NDK (Native Development Kit)**
   - Ensure that the Android Native Development Kit (NDK) is installed. You can install it via Android Studio:
     - Go to `File` > `Project Structure` > `SDK Location`.
     - Click on `Download` in the `NDK` section if it's not already installed.

2. **Create a New C++ File**
   - In your Android Studio project, navigate to the `src/main/cpp` directory (create it if it doesn’t exist).
   - Right-click on the `cpp` directory, select `New` > `C++ Class` or `C++ Source File`, and create your C++ file.

3. **Configure `CMakeLists.txt`**
   - Android Studio uses CMake to build C++ code. Ensure you have a `CMakeLists.txt` file in your `cpp` directory.
   - If you don't have one, create it with the following content:
     ```cmake
     cmake_minimum_required(VERSION 3.4.1)
     add_library( # Specifies the name of the library.
             native-lin
     
             # Sets the library as a shared library
             SHARED
     
             # Provides a relative path to your source file(s).
             native-lib.cpp
     )
     ```

4. **Link C++ Code to Your Android Project**
   - Open your `build.gradle` file (app level) and add the following under the `android` block:
     ```groovy
     android {
     ...
       externalNativeBuild {
           cmake {
               path "src/main/cpp/CMakeLists.txt"
           }
       }
     ...
     }
     ```
   - Sync your project.

5. **Load C++ Code in Kotlin**
   - To load your C++ library in your Android app, add the following to your `MainActivity.kt`:
     ```kotlin
     companion object {
       init {
         System.loadLibrary("native-lib")
       }
     }
     ```

6. **Declare Native Methods in Kotlin**
   - Declare the native methods you want to use in your Kotlin code:
     ```kotlin
     external fun stringFromCPP(): String?
     ```

7. **Implement Native Methods in C++**
   - In your C++ file (`native-lib.cpp`), implement the native methods:
     ```cpp
      #include <jni.h>
      #include <string>
      
      extern "C" JNIEXPORT jstring JNICALL
      Java_com_proximie_democpp_MainActivity_stringFromCPP(JNIEnv* env,jobject ) {
          std::string hello = "Hello from C++";
          return env->NewStringUTF(hello.c_str());
      }
     ```
