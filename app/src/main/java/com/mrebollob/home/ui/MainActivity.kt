package com.mrebollob.home.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.github.romychab.slidetounlock.renderers.ScaleRenderer
import com.github.romychab.slidetounlock.sliders.HorizontalSlider
import com.mrebollob.home.R.layout
import com.mrebollob.home.data.NetworkDataSource
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.openButton
import kotlinx.android.synthetic.main.activity_main.slide1


class MainActivity : Activity() {

  private val disposable = CompositeDisposable()
  private val networkDataSource = NetworkDataSource()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    Fabric.with(this, Crashlytics())
    initView()
  }

  private fun initView() {
    openButton.setOnClickListener { openStreetDoor() }

    slide1.setRenderer(ScaleRenderer())
    slide1.setSlider(HorizontalSlider())
    slide1.addSlideListener { slider, done ->
      if (done) {
        slider.reset()
      }
    }
  }

  private fun openStreetDoor() {
    openButton.isEnabled = false
    disposable.add(networkDataSource.openStreetDoor()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ status ->
          Log.d(TAG, "Bien $status")
          openButton.isEnabled = true
        }, { error ->
          Log.e(TAG, "Recalculate route error", error)
          openButton.isEnabled = true
        }))
  }

  override fun onStop() {
    super.onStop()
    disposable.clear()
  }

  companion object {
    private val TAG = MainActivity::class.java.simpleName
  }
}
