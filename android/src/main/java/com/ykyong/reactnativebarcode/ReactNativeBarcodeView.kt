package com.ykyong.reactnativebarcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter

class ReactNativeBarcodeView : ImageView {
  private var value: String? = null
  private var format: BarcodeFormat = BarcodeFormat.QR_CODE
  private var foregroundColor: Int = Color.BLACK
  private var backgroundColorValue: Int = Color.WHITE

  constructor(context: Context?) : super(context) {
    initView()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    initView()
  }

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  ) {
    initView()
  }

  private fun initView() {
    scaleType = ScaleType.FIT_CENTER
  }

  fun setValue(value: String?) {
    this.value = value
    updateBarcode()
  }

  fun setFormat(format: BarcodeFormat) {
    this.format = format
    updateBarcode()
  }

  fun setForegroundColor(color: Int?) {
    foregroundColor = color ?: Color.BLACK
    updateBarcode()
  }

  fun setBarcodeBackgroundColor(color: Int?) {
    backgroundColorValue = color ?: Color.WHITE
    updateBarcode()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    if (w != oldw || h != oldh) {
      updateBarcode()
    }
  }

  private fun updateBarcode() {
    val content = value
    val width = width
    val height = height

    if (content.isNullOrBlank()) {
      setImageBitmap(null)
      return
    }

    if (width <= 0 || height <= 0) {
      return
    }

    try {
      val hints = mapOf(EncodeHintType.MARGIN to 1)
      val bitMatrix = MultiFormatWriter().encode(content, format, width, height, hints)
      val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
      for (x in 0 until width) {
        for (y in 0 until height) {
          bitmap.setPixel(x, y, if (bitMatrix[x, y]) foregroundColor else backgroundColorValue)
        }
      }
      setImageBitmap(bitmap)
    } catch (exception: Exception) {
      setImageBitmap(null)
      emitError("encode_failed", exception.message ?: "Failed to encode barcode")
    }
  }

  private fun emitError(code: String, message: String) {
    val reactContext = context as? ReactContext ?: return
    val eventDispatcher: EventDispatcher =
      UIManagerHelper.getEventDispatcherForReactTag(reactContext, id) ?: return
    val surfaceId = UIManagerHelper.getSurfaceId(reactContext)
    eventDispatcher.dispatchEvent(BarcodeErrorEvent(surfaceId, id, message, code))
  }
}

@Suppress("DEPRECATION")
class BarcodeErrorEvent(
  surfaceId: Int,
  viewId: Int,
  private val message: String,
  private val code: String?
) : Event<BarcodeErrorEvent>(surfaceId, viewId) {
  override fun getEventName(): String = "topError"

  override fun dispatch(rctEventEmitter: RCTEventEmitter) {
    val map = Arguments.createMap()
    map.putString("message", message)
    if (code != null) {
      map.putString("code", code)
    }
    rctEventEmitter.receiveEvent(viewTag, eventName, map)
  }
}
