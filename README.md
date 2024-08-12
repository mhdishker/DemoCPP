## Steps to Add C++ Code to Your Jetpack compose project with groovy DSL

1. **Set Up NDK (Native Development Kit)**
   - Ensure that the Android Native Development Kit (NDK) is installed. You can install it via Android Studio:
     - Go to `Tools` > `SDK Manager` and select the Show Package Details checkbox
     - In the `SDK Tools` tab, select the NDK checkbox and the checkboxes below it that correspond to the NDK versions you want to install.
       <img width="986" alt="image" src="https://github.com/user-attachments/assets/c7a6b54f-90a2-45e5-b39d-83a7db34d2ad">



2. **Install CMake**
   - CMake is required to build C++ code in Android Studio. Ensure CMake is installed:
     - Go to `Tools` > `SDK Manager` and select the Show Package Details checkbox
     - In the `SDK Tools` tab, check `CMake` and click `Apply` to install it if it's not already installed.
       <img width="985" alt="image" src="https://github.com/user-attachments/assets/1817196b-0f38-4487-90a6-26b7a5815d53">

3. **Configure `CMakeLists.txt`**
   - In your Android Studio project, navigate to the `src/main/cpp` directory (create it if it doesn’t exist).
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
     <img width="982" alt="image" src="https://github.com/user-attachments/assets/cb41bae3-3ee0-473a-adb6-08f5232a19bb">


4. **Create a New C++ File**
   - Now create the `native-lib.cpp`: Right-click on the `cpp` directory, select `New` > `C++ Class` or `C++ Source File` and name it "native-lib.cpp" or any other name
<img width="348" alt="image" src="https://github.com/user-attachments/assets/dde5b649-8e31-423b-b2be-83d06f35619d">

5. **Link C++ Code to Your Android Project**
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

6. **Load C++ Code in Kotlin**
   - To load your C++ library in your Android app, add the following to your `MainActivity.kt`:
     ```kotlin
     companion object {
       init {
         System.loadLibrary("native-lib")
       }
     }
     ```

7. **Declare Native Methods in Kotlin**
   - Declare the native methods you want to use in your Kotlin code:
     ```kotlin
     external fun stringFromCPP(): String?
     ```

8. **Implement Native Methods in C++**
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
