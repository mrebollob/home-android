package com.mrebollob.home.data

import com.mrebollob.home.domain.Status
import io.reactivex.Observable
import retrofit2.http.GET


interface HomeApi {

  @GET("/home/open/FRONT_DOOR")
  fun openStreetDoor(): Observable<Status>
}