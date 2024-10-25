import 'package:flutter_test/flutter_test.dart';
import 'package:dashcam_player/dashcam_player.dart';
import 'package:dashcam_player/dashcam_player_platform_interface.dart';
import 'package:dashcam_player/dashcam_player_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDashcamPlayerPlatform
    with MockPlatformInterfaceMixin
    implements DashcamPlayerPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final DashcamPlayerPlatform initialPlatform = DashcamPlayerPlatform.instance;

  test('$MethodChannelDashcamPlayer is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDashcamPlayer>());
  });

  test('getPlatformVersion', () async {
    DashcamPlayer dashcamPlayerPlugin = DashcamPlayer();
    MockDashcamPlayerPlatform fakePlatform = MockDashcamPlayerPlatform();
    DashcamPlayerPlatform.instance = fakePlatform;

    expect(await dashcamPlayerPlugin.getPlatformVersion(), '42');
  });
}
