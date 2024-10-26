import 'dashcam_player_platform_interface.dart';

class DashcamPlayer {
  Future<String?> getPlatformVersion() {
    return DashcamPlayerPlatform.instance.getPlatformVersion();
  }

  Future<void> playVideo() {
    return DashcamPlayerPlatform.instance.playVideo();
  }

  Future<void> pauseVideo() {
    return DashcamPlayerPlatform.instance.pauseVideo();
  }

  Future<void> seekTo(double position) {
    return DashcamPlayerPlatform.instance.seekTo(position);
  }

  Future<void> replayVideo() {
    return DashcamPlayerPlatform.instance.replayVideo();
  }

  Future<void> stopVideo() {
    return DashcamPlayerPlatform.instance.stopVideo();
  }
  
  Future<int?> duration() {
    return DashcamPlayerPlatform.instance.duration();
  }
}
