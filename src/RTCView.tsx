import React, { useImperativeHandle, useRef } from 'react';
import { findNodeHandle, requireNativeComponent, NativeModules, ViewProps } from 'react-native';

/**
 * Native prop validation was removed from RN in:
 * https://github.com/facebook/react-native/commit/8dc3ba0444c94d9bbb66295b5af885bff9b9cd34
 *
 * So we list them here for documentation purposes.
 */

interface RTCVideoViewProps extends ViewProps {
  /**
   * Indicates whether the video specified by {@link #streamURL} should be
   * mirrored during rendering. Commonly, applications choose to mirror the
   * user-facing camera.
   *
   * mirror: boolean
   */
  mirror?: boolean;

  /**
   * In the fashion of
   * https://www.w3.org/TR/html5/embedded-content-0.html#dom-video-videowidth
   * and https://www.w3.org/TR/html5/rendering.html#video-object-fit,
   * resembles the CSS style object-fit.
   *
   * objectFit: 'contain' | 'cover'
   */
  objectFit?: 'contain' | 'cover';

  /**
   * URL / id of the stream that should be rendered.
   *
   * streamURL: string
   */
  streamURL: string;
  /**
   * Similarly to the CSS property z-index, specifies the z-order of this
   * RTCView in the stacking space of all RTCViews. When RTCViews overlap,
   * zOrder determines which one covers the other. An RTCView with a larger
   * zOrder generally covers an RTCView with a lower one.
   *
   * Non-overlapping RTCViews may safely share a z-order (because one does not
   * have to cover the other).
   *
   * The support for zOrder is platform-dependent and/or
   * implementation-specific. Thus, specifying a value for zOrder is to be
   * thought of as giving a hint rather than as imposing a requirement. For
   * example, video renderers such as RTCView are commonly implemented using
   * OpenGL and OpenGL views may have different numbers of layers in their
   * stacking space. Android has three: a layer bellow the window (aka
   * default), a layer bellow the window again but above the previous layer
   * (aka media overlay), and above the window. Consequently, it is advisable
   * to limit the number of utilized layers in the stacking space to the
   * minimum sufficient for the desired display. For example, a video call
   * application usually needs a maximum of two zOrder values: 0 for the
   * remote video(s) which appear in the background, and 1 for the local
   * video(s) which appear above the remote video(s).
   *
   * zOrder: number
   */
  zOrder?: number;

  onRecordingEnd?: (path: string) => void;
}

const RTCVideoView = requireNativeComponent<RTCVideoViewProps>('RTCVideoView');
const { UIManager } = NativeModules;

if (UIManager) {
    UIManager.genericDirectEventTypes = {
        ...UIManager.genericDirectEventTypes,
        onCaptureEnd: { registrationName: 'onCaptureEnd' },
        onRecordingEnd: {
            registrationName: 'onRecordingEnd',
        },
    };
}

const RTCView = (props: RTCVideoViewProps, ref) => {
    const reactTag = useRef<number | null>(null);

    useImperativeHandle(ref, () => {
        return {
            capture: () => {
                console.log(reactTag);
            },
            startRecording: () => {
                UIManager.dispatchViewManagerCommand(
                    reactTag.current,
                    UIManager.getViewManagerConfig('RTCVideoView').Commands.startRecording.toString(),
                    []
                );
            },
            stopRecording: () => {
                UIManager.dispatchViewManagerCommand(
                    reactTag.current,
                    UIManager.getViewManagerConfig('RTCVideoView').Commands.stopRecording.toString(),
                    []
                );
            },
        };
    });

    return (
        <RTCVideoView
            ref={ref => {
                reactTag.current = findNodeHandle(ref);
            }}
            {...props}
        />
    );
};

export default React.forwardRef(RTCView);