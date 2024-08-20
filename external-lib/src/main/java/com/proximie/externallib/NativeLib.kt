package com.proximie.externallib


class NativeLib {

    // Load the native library
    init {
        System.loadLibrary("external-lib")
    }

    // Declare the native method
    external fun stringFromAAR(): String

}