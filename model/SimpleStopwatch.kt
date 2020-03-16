package com.optimus.simplestopwatch.model

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Dmitriy Chebotar on 15.03.2020.
 */
class SimpleStopwatch(private val listener: (time: Long) -> Unit) {

    private var time = 0L
    private var isStarted = true
    private var setupStopwatch: Observable<Long>

    lateinit var disposable: Disposable


    init {
        setupStopwatch = setupStopwatch()
    }

    private fun setupStopwatch(): Observable<Long> {
        return Observable.interval(1, TimeUnit.MILLISECONDS)
            .flatMap {
                return@flatMap Observable.create<Long> {
                    it.onNext(time)
                    time++
                }
            }
//        return Observable.create{subscriber ->
//            while (isStarted){
//                time++
//                Thread.sleep(1)
//                subscriber.onNext(time)
//            }
//            subscriber.onComplete()
//        }
    }

    @Synchronized
    fun start(): SimpleStopwatch {
        isStarted = true
        disposable = setupStopwatch
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (isStarted) {
                    Log.e("M_SimpleStopwatch", "$it")
                    listener.invoke(it)
                } else {
                    disposable.dispose()
                }
            }, {
                Log.e("M_SimpleStopwatch", "error")
            })
        return this
    }

    @Synchronized
    fun pause(): SimpleStopwatch {
        isStarted = false
        return this
    }

    @Synchronized
    fun reset(): SimpleStopwatch {
        isStarted = false
        time = 0
        disposable.dispose()
        return this
    }

    fun holdData() {

    }


}