package com.ykyong.reactnativebarcode

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.ReactNativeBarcodeViewManagerInterface
import com.facebook.react.viewmanagers.ReactNativeBarcodeViewManagerDelegate

@ReactModule(name = ReactNativeBarcodeViewManager.NAME)
class ReactNativeBarcodeViewManager : SimpleViewManager<ReactNativeBarcodeView>(),
  ReactNativeBarcodeViewManagerInterface<ReactNativeBarcodeView> {
  private val mDelegate: ViewManagerDelegate<ReactNativeBarcodeView>

  init {
    mDelegate = ReactNativeBarcodeViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<ReactNativeBarcodeView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ReactNativeBarcodeView {
    return ReactNativeBarcodeView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: ReactNativeBarcodeView?, color: Int?) {
    view?.setBackgroundColor(color ?: Color.TRANSPARENT)
  }

  companion object {
    const val NAME = "ReactNativeBarcodeView"
  }
}
