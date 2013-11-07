/*
 * PersonDetector.cpp
 *
 *  Created on: 2013年11月7日
 *      Author: marshal
 */

#include <jni.h>
#include <stdio.h>
#include <iostream>

#include <android/log.h>
//#include <opencv2/opencv.hpp>

#include "shiqichan_singinapp_PersonDetector.h"

using namespace std;
//using namespace cv;

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "person_detector", __VA_ARGS__))

JNIEXPORT jint JNICALL Java_shiqichan_singinapp_PersonDetector_detect(
		JNIEnv * env, jobject thiz) {
	LOGI(">>测试通过");
	return 10;
}

