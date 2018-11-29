package com.dmide.revolutassignment.app

import android.app.Activity
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class LifecycleObserver @Inject constructor(application: CurrenciesApplication) {

    val lifecycleSubject = BehaviorSubject.create<Event>()

    init {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityResumed(p0: Activity?) {
                lifecycleSubject.onNext(Event.Resume)
            }

            override fun onActivityPaused(p0: Activity?) {
                lifecycleSubject.onNext(Event.Pause)
            }
        })
    }

    sealed class Event {
        object Resume : Event()
        object Pause : Event()
    }
}