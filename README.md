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

## Steps to integrating `libbinary-lib.so` into an Android Project

This guide explains how to integrate a precompiled `.so` library (`libbinary-lib.so`) into an Android project that already has an existing C++ source file (`native-lib.cpp`).

1. **Modify `CMakeLists.txt`**

Update your `CMakeLists.txt` file to include both the existing `native-lib.cpp` and `libbinary-lib.cbb`.

```cmake

add_library(
    binary-lib
    SHARED
    binary-lib.cpp )

target_link_libraries(
    binary-lib
    ${log-lib} )
```

2. **Building the `.so` File**

- Build the Project: Run the following command in the project root to build your C++ code into a `.so` file:

```bash
./gradlew assembleDebug
```
The .so files will be generated for different architectures (e.g., armeabi-v7a, arm64-v8a, x86, x86_64) in the app/build/intermediates/cmake/debug/obj/ directory.

- Locate the .so Files:
  The .so files will be located in:

```bash
app/build/intermediates/cxx/debug/obj/<architecture>/libbinary-lib.so
```
<architecture> refers to armeabi-v7a, arm64-v8a, x86, or x86_64.


![image](https://github.com/user-attachments/assets/4822d406-8b69-4c9c-9bc2-e7c94ecfba14)

3. **Include the `.so` Files in Your Project**

Create a `libs` directory under `app` and subdirectories for each architecture:

```bash
mkdir -p app/libs/armeabi-v7a
mkdir -p app/libs/arm64-v8a
mkdir -p app/libs/x86
mkdir -p app/libs/x86_64
```
Copy the .so Files:
Copy the compiled .so files into their respective architecture directories:

```bash
cp app/build/intermediates/cxx/debug/obj/armeabi-v7a/libbinary-lib.so  app/libs/armeabi-v7a/
cp app/build/intermediates/cxx/debug/obj/arm64-v8a/libbinary-lib.so  app/libs/arm64-v8a/
cp app/build/intermediates/cxx/debug/obj/x86/libbinary-lib.so  app/libs/x86/
cp app/build/intermediates/cxx/debug/obj/x86_64/libbinary-lib.so  app/libs/x86_64/
```
![image](https://github.com/user-attachments/assets/c0470d46-d0aa-49f2-8378-2a6f24f1794c)


Configure build.gradle to Include the .so Files:
Ensure that the `build.gradle` file includes the path to the libs directory:

```groovy
android {
    ...
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
}
```

4. **Load the Binary Library in Your Kotlin Code**

Load the .so Library:In your MainActivity (or relevant class), load the binary library using System.loadLibrary():
```kotlin
    companion object {
        init {
            System.loadLibrary("native-lib")
            System.loadLibrary("binary-lib")
        }
    }
```
5. **Optional - Cleanup**

Optional: Remove the .cpp File:

If you no longer need the source code in the project, you can remove the binary-lib.cpp file:
```bash
rm app/src/main/cpp/binary-lib.cpp
```

Rebuild the project to ensure everything is working:
```bash
./gradlew assembleDebug
```


## Steps to Add aar library to Your Jetpack compose project
you can create it in this project or in other project. For demo purpos we will create it in the project, please checkout [build-aar](https://github.com/mhdishker/DemoCPP/tree/build-aar) branch,  the last commit represent the below steps

1. **create AAR library file**
   Go to `File > New > New Module` and Select `Android Library` and click Next

Name the library module `external-lib` then Click Finish.

<img width="854" alt="image" src="https://github.com/user-attachments/assets/bf6d2542-c000-4d5f-b0c4-bd215308b1cd">

Right-click on the external-lib module and select `Add C++ to Project`.

<img width="523" alt="image" src="https://github.com/user-attachments/assets/440eba55-1483-4656-8a5a-4b7b1a521f4f">

2. **Set Up CMakeLists.txt**

In the external-lib module, open the CMakeLists.txt file and ensure it includes the following:

```cmake
cmake_minimum_required(VERSION 3.4.1)
project("external-lib")

add_library( # Sets the name of the library.
             external-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/external-lib.cpp )

find_library( # Sets the path of the NDK library.
              log-lib

              # Specifies the NDK library that you want to link to.
              log )

target_link_libraries( # Links your external library with the NDK library.
                       external-lib
                       ${log-lib} )
```
3. **Write the C++ Code**

In the external-lib module, navigate to src/main/cpp/ and edit the external-lib.cpp file with the following code:

```cpp
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_externallib_NativeLib_stringFromAAR(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++ in AAR!";
    return env->NewStringUTF(hello.c_str());
}
```
4. **Create the Kotlin Class**
5.
In the external-lib module, navigate to src/main/java/com/proximie/externallib/.

Create a new Kotlin file named NativeLib.kt with the following code:

```kotlin
package com.proximie.externallib

class NativeLib {
    // Load the native library
    init {
        System.loadLibrary("external-lib")
    }

    // Declare the native method
    external fun stringFromAAR(): String

}
```

5. **Build the AAR File**

Build your projectm, the AAR file will be generated in external-lib/build/outputs/aar/.

6. **Add the AAR to Your Main Project**
   Copy the AAR file from external-lib/build/outputs/aar/ to the libs directory in your main project’s app module.

<img width="382" alt="image" src="https://github.com/user-attachments/assets/8d34358c-1e3a-4db1-9b0c-b35c081249c0">

Modify the app/build.gradle file to include the AAR as a dependency:

```groovy
dependencies {
    implementation files('libs/external-lib-release.aar')
}
```
Use the NativeLib Class in Your Main Project:

In your main project’s MainActivity or any other activity, use the NativeLib class:

```kotlin
import com.example.externallib.NativeLib

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = NativeLib().stringFromAAR(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
```
