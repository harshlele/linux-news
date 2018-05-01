package com.hllabs.linuxnews

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

//Simple Event bus using RXAndroid
enum class FeedItemsBus {

    INSTANCE;

    private val bus = PublishSubject.create<ArrayList<NewsArticle>>() // the actual publisher handling all of the events

    fun send(message: ArrayList<NewsArticle>) {
        bus.onNext(message) // the message being sent to all subscribers
    }

    fun toObserverable(): Observable<ArrayList<NewsArticle>> {
        return bus // return the publisher itself as an observable to subscribe to
    }

}