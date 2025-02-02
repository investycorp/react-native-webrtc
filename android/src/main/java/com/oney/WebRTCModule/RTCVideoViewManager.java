package com.oney.WebRTCModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

import java.util.Map;


public class RTCVideoViewManager extends SimpleViewManager<WebRTCView> {
  private static final String REACT_CLASS = "RTCVideoView";

  public final int COMMAND_CAPTURE = 1;
  public final int COMMAND_START_RECORDING = 2;
  public final int COMMAND_STOP_RECORDING = 3;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public WebRTCView createViewInstance(ThemedReactContext context) {
    return new WebRTCView(context);
  }

  @Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of(
            "capture", COMMAND_CAPTURE,
            "startRecording", COMMAND_START_RECORDING,
            "stopRecording", COMMAND_STOP_RECORDING
    );
  }

  @Nullable
  @Override
  public Map getExportedCustomBubblingEventTypeConstants() {
    return MapBuilder
            .builder()
            .put(
               "onCaptureEnd", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onCaptureEnd"))
            )
            .put(
                "onRecordingEnd", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onRecordingEnd"))
            )
            .build();
  }

  @Override
  public void receiveCommand(@NonNull WebRTCView root, String commandId, @Nullable ReadableArray args) {
    super.receiveCommand(root, commandId, args);

    int commandIdInt = Integer.parseInt(commandId);

    switch (commandIdInt) {
      case COMMAND_CAPTURE:
        root.capture();
        break;
      case COMMAND_START_RECORDING:
        root.recordingStart();
        break;
      case COMMAND_STOP_RECORDING:
        root.recordingStop();
        break;
    }
  }

  /**
   * Sets the indicator which determines whether a specific {@link WebRTCView}
   * is to mirror the video specified by {@code streamURL} during its rendering.
   * For more details, refer to the documentation of the {@code mirror} property
   * of the JavaScript counterpart of {@code WebRTCView} i.e. {@code RTCView}.
   *
   * @param view The {@code WebRTCView} on which the specified {@code mirror} is
   * to be set.
   * @param mirror If the specified {@code WebRTCView} is to mirror the video
   * specified by its associated {@code streamURL} during its rendering,
   * {@code true}; otherwise, {@code false}.
   */
  @ReactProp(name = "mirror")
  public void setMirror(WebRTCView view, boolean mirror) {
    view.setMirror(mirror);
  }

  /**
   * In the fashion of
   * https://www.w3.org/TR/html5/embedded-content-0.html#dom-video-videowidth
   * and https://www.w3.org/TR/html5/rendering.html#video-object-fit, resembles
   * the CSS style {@code object-fit}.
   *
   * @param view The {@code WebRTCView} on which the specified {@code objectFit}
   * is to be set.
   * @param objectFit For details, refer to the documentation of the
   * {@code objectFit} property of the JavaScript counterpart of
   * {@code WebRTCView} i.e. {@code RTCView}.
   */
  @ReactProp(name = "objectFit")
  public void setObjectFit(WebRTCView view, String objectFit) {
    view.setObjectFit(objectFit);
  }

  @ReactProp(name = "streamURL")
  public void setStreamURL(WebRTCView view, String streamURL) {
    view.setStreamURL(streamURL);
  }

  /**
   * Sets the z-order of a specific {@link WebRTCView} in the stacking space of
   * all {@code WebRTCView}s. For more details, refer to the documentation of
   * the {@code zOrder} property of the JavaScript counterpart of
   * {@code WebRTCView} i.e. {@code RTCView}.
   *
   * @param view The {@code WebRTCView} on which the specified {@code zOrder} is
   * to be set.
   * @param zOrder The z-order to set on the specified {@code WebRTCView}.
   */
  @ReactProp(name = "zOrder")
  public void setZOrder(WebRTCView view, int zOrder) {
    view.setZOrder(zOrder);
  }
}
