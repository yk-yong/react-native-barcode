package com.ykyong.reactnativebarcode

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.google.zxing.BarcodeFormat

@ReactModule(name = ReactNativeBarcodeViewManager.NAME)
class ReactNativeBarcodeViewManager : SimpleViewManager<ReactNativeBarcodeView>() {

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ReactNativeBarcodeView {
    return ReactNativeBarcodeView(context)
  }

  @ReactProp(name = "value")
  fun setValue(view: ReactNativeBarcodeView?, value: String?) {
    view?.setValue(value)
  }

  @ReactProp(name = "format")
  fun setFormat(view: ReactNativeBarcodeView?, format: String?) {
    view?.setFormat(parseFormat(format))
  }

  @ReactProp(name = "foregroundColor")
  fun setForegroundColor(view: ReactNativeBarcodeView?, color: Int?) {
    view?.setForegroundColor(color)
  }

  @ReactProp(name = "backgroundColor")
  fun setBackgroundColor(view: ReactNativeBarcodeView?, color: Int?) {
    view?.setBarcodeBackgroundColor(color)
  }

  private fun parseFormat(format: String?): BarcodeFormat {
    return when (format?.lowercase()) {
      "code128" -> BarcodeFormat.CODE_128
      "code39" -> BarcodeFormat.CODE_39
      "ean13" -> BarcodeFormat.EAN_13
      "ean8" -> BarcodeFormat.EAN_8
      "upca" -> BarcodeFormat.UPC_A
      "upce" -> BarcodeFormat.UPC_E
      "itf" -> BarcodeFormat.ITF
      "pdf417" -> BarcodeFormat.PDF_417
      "aztec" -> BarcodeFormat.AZTEC
      "datamatrix" -> BarcodeFormat.DATA_MATRIX
      else -> BarcodeFormat.QR_CODE
    }
  }

  companion object {
    const val NAME = "ReactNativeBarcodeView"
  }
}
