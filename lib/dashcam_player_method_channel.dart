import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dashcam_player_platform_interface.dart';

/// An implementation of [DashcamPlayerPlatform] that uses method channels.
class MethodChannelDashcamPlayer extends DashcamPlayerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dashcam_player');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> playVideo() async {
    await methodChannel.invokeMethod<void>('playVideo');
  }

  @override
  Future<void> pauseVideo() async {
    await methodChannel.invokeMethod<void>('pauseVideo');
  }

  @override
  Future<void> seekTo(double position) async {
    await methodChannel.invokeMethod<void>('seekTo', {
      'position': position,
    });
  }

  @override
  Future<void> replayVideo() async {
    await methodChannel.invokeMethod<void>('replay');
  }

  @override
  Future<void> stopVideo() async {
    await methodChannel.invokeMethod<void>('stopVideo');
  }
  
  Future<int?> duration() async {
    int? duration = await methodChannel.invokeMethod<int>('getDuration');
    return duration;
  }
}
