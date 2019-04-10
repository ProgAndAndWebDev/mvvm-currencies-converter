package com.dmide.currencies.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

abstract class ActivityLifecycleCallbacksAdapter: Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity?) = Unit

    override fun onActivityResumed(p0: Activity?) = Unit

    override fun onActivityStarted(p0: Activity?) = Unit

    override fun onActivityDestroyed(p0: Activity?) = Unit

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) = Unit

    override fun onActivityStopped(p0: Activity?) = Unit

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) = Unit
}