package ir.modir.app.utils

import ir.modir.app.BuildConfig

fun <T> onBuildTypes(
    release: T,
    debug: T,
): T {
    return when {
        BuildConfig.DEBUG -> debug
        else -> release
    }
}

fun areWeInRelease() = BuildConfig.BUILD_TYPE == "release"