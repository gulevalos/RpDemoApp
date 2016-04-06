#include <string.h>
#include <jni.h>

jstring
Java_com_example_mardiak_marek_rpdemoapp_JniFragment_stringFromJNI( JNIEnv* env,
                                                  jobject thiz, jstring radioVersion)
{

    //creating InfoServiceJni instance and calling method on it
    jclass clsInfoServiceJni = (*env)->FindClass(env, "com/example/mardiak/marek/rpdemoapp/hellojni/InfoServiceJni");
    jmethodID infoServiceJniMIDInit = (*env)->GetMethodID(env, clsInfoServiceJni, "<init>", "()V");
    jobject objInfoService=(*env)->NewObject(env, clsInfoServiceJni, infoServiceJniMIDInit);
    jmethodID infoServiceJniMIDPhoneInfo = (*env)->GetMethodID(env, clsInfoServiceJni, "phoneInfo", "()Ljava/lang/String;");
    jstring resultStr = (*env)->CallObjectMethod(env, objInfoService, infoServiceJniMIDPhoneInfo);

    //calling method on java wrappee object
    jclass clsThiz = (*env)->GetObjectClass(env, thiz);
    jmethodID wrapperMIDshowToast = (*env)->GetMethodID(env, clsThiz, "showToast", "(Ljava/lang/String;)V");
    char *buf = "Hi from C world!!!!";
    jstring toastMessage = (*env)->NewStringUTF(env, buf);
    (*env)->CallVoidMethod(env, thiz, wrapperMIDshowToast, toastMessage);
    (*env)->CallVoidMethod(env, thiz, wrapperMIDshowToast, radioVersion);

    const char *resultCStr = (*env)->GetStringUTFChars(env, resultStr, NULL);
    return (*env)->NewStringUTF(env, resultCStr);
}