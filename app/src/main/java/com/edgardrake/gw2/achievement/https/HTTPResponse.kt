package com.edgardrake.gw2.achievement.https

import io.reactivex.Observable
import retrofit2.Response

abstract class HTTPResponse<T>(): Observable<Response<T>>() {
}