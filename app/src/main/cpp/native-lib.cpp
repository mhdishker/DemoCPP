#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_proximie_democpp_MainActivity_stringFromCPP(JNIEnv* env,jobject ) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
