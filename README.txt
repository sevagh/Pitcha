*******************
BRANCH: NDK_ATTEMPT
*******************

This attempt did not go very well. I will leave the branch for future
reference in case I need to mess around with the NDK but I do not
believe that it will give me any significant performance gains, so I
will discontinue working on it.

To create the JNI function definition, I wrote the Java code first (in
com.sevag.recording.AudioRecorder, and then from
app/build/intermediates/classes/debug, I executed the command 'javah
-jni com.sevag.pitcha.recording.AudioRecorder'. This generated a file
called 'com_sevag_pitcha_recording_AudioRecorder.h', which contained
the function definition that I put into the pitchalib.c file:

JNIEXPORT jdouble JNICALL Java_com_sevag_pitcha_recording_AudioRecorder_get_1pitch_1from_1short
  (JNIEnv *, jclass, jshortArray);

======================
GOOGLE PLAY STORE LINK
======================

https://play.google.com/store/apps/details?id=com.sevag.pitcha

======
PITCHA
======

Author: Sevag Hanssian <sevag.hanssian@gmail.com>

===========
DESCRIPTION
===========

This is a pitch detector for Android.

It uses the McLeod Pitch Method, adapted from TarsosDSP:

https://github.com/JorenSix/TarsosDSP

=======
INSTALL
=======

The repository is an Android Studio project.

To install the app, download it from the Play Store link above, or
download the app-release.apk file in app/ from this repo.

=================
BUGS AND FEATURES
=================

Feel free to report or submit fixes for bugs, and to request request
or submit features.
