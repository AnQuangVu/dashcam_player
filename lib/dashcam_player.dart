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

  Future<int?> getDuration(String path) {
    return DashcamPlayerPlatform.instance.getDuration(path);
  }

  Future<String?> getMetadataInStream() {
    return DashcamPlayerPlatform.instance.getMetadataInStream();
  }

  Future<void> playNextFileInStream(String path) {
    return DashcamPlayerPlatform.instance.playNextFileInStream(path);
  }
}
