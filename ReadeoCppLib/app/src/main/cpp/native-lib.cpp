#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_imie_a2dev_teamculte_readeocpplib_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
