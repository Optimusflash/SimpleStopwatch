package com.optimus.simplestopwatch.model

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Dmitriy Chebotar on 15.03.2020.
 */
abstract class SimpleStopwatch  {

    private var time = 0L
    private var isStarted = true
    private var setupStopwatch: Observable<Long>

    lateinit var disposable: Disposable

    abstract fun onTick(time: Long)

    init {
        setupStopwatch = setupStopwatch()
    }

    private fun setupStopwatch(): Observable<Long> {
        return Observable.create{subscriber ->
            while (isStarted){
                time++
                Thread.sleep(10)
                subscriber.onNext(time*10L)
            }
            subscriber.onComplete()
        }
    }

    @Synchronized
    fun start(): SimpleStopwatch{
        isStarted = true
        disposable = setupStopwatch
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                Log.e("M_SimpleStopwatch", "$it")
                onTick(it)
            }, {
                Log.e("M_SimpleStopwatch", "error")
            })
        return this
    }
    @Synchronized
    fun pause(): SimpleStopwatch{
        isStarted = false
        return this
    }
    @Synchronized
    fun reset(): SimpleStopwatch{
        if (!isStarted) {
            time = 0
            disposable.dispose()
        }
        return this
    }

    fun holdData(){

    }


}