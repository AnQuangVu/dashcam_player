import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'dashcam_player_method_channel.dart';

abstract class DashcamPlayerPlatform extends PlatformInterface {
  /// Constructs a DashcamPlayerPlatform.
  DashcamPlayerPlatform() : super(token: _token);

  static final Object _token = Object();

  static DashcamPlayerPlatform _instance = MethodChannelDashcamPlayer();

  /// The default instance of [DashcamPlayerPlatform] to use.
  ///
  /// Defaults to [MethodChannelDashcamPlayer].
  static DashcamPlayerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DashcamPlayerPlatform] when
  /// they register themselves.
  static set instance(DashcamPlayerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  /// Play the video.
  Future<void> playVideo() {
    throw UnimplementedError('playVideo() has not been implemented.');
  }

  Future<void> pauseVideo() {
    throw UnimplementedError('pauseVideo() has not been implemented.');
  }

  Future<void> seekTo(double position) {
    throw UnimplementedError('seekTo() has not been implemented.');
  }

  Future<void> replayVideo() {
    throw UnimplementedError('replayVideo() has not been implemented.');
  }

  Future<void> stopVideo() {
    throw UnimplementedError('stopVideo() has not been implemented.');
  }

  Future<int?> duration() {
    throw UnimplementedError('duration() has not been implemented.');
  }
}
