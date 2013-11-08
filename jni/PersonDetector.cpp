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
#include <opencv2/opencv.hpp>

#include "shiqichan_singinapp_PersonDetector.h"

using namespace std;
using namespace cv;

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "person_detector", __VA_ARGS__))

JNIEXPORT jint JNICALL Java_shiqichan_singinapp_PersonDetector_detect(
		JNIEnv * env, jobject thiz, jbyteArray frameData, jint width, jint height) {
	int count = 0; //返回值
	jbyte* yuv = env->GetByteArrayElements(frameData, 0);
	Mat frame(height, width, CV_8UC1, (unsigned char *) yuv); //当前帧

	stringstream strm; //用于日志

	Size dsize = Size(frame.cols * .3, frame.rows * .3);
	Mat currFrame = Mat(dsize, CV_8UC1);
	resize(frame, currFrame, dsize);

	//使用FAST算法获取角点
	FastFeatureDetector fast(40);
	vector<KeyPoint> v;

	fast.detect(currFrame, v);
	count = v.size();

	env->ReleaseByteArrayElements(frameData, yuv, 0);
	return (jint) count;
}

