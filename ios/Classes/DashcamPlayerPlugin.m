#import "DashcamPlayerPlugin.h"
#import "MobileVLCKit/MobileVLCKit.h"
#import "PlayerFactory.h"
#import "G3StreamFactory.h"
#import "G3StreamView.h"
#import "G3StreamOnlineFactory.h"

@implementation DashcamPlayerPlugin
SharedMetaData* metaDataInStream;
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"dashcam_player"
            binaryMessenger:[registrar messenger]];
  DashcamPlayerPlugin* instance = [[DashcamPlayerPlugin alloc] init];
  instance.mediaPlayer = [[VLCMediaPlayer alloc] init];
  instance.queuePlayer = [[AVQueuePlayer alloc] init];
  instance.playerLayer = [AVPlayerLayer playerLayerWithPlayer:instance.queuePlayer];
  PlayerFactory *factory = [[PlayerFactory alloc] initWithMessenger:registrar.messenger withMediaPlayer:instance.mediaPlayer];
  metaDataInStream = [[SharedMetaData alloc] init];
  G3StreamFactory *g3Factory = [[G3StreamFactory alloc] initWithMessenger:registrar.messenger metaDaInStream:metaDataInStream];
  G3StreamOnlineFactory *g3StreamOnlineFactory = [[G3StreamOnlineFactory alloc] initWithMessenger:registrar.messenger withQueuePlayer:instance.queuePlayer withPlayerLayer:instance.playerLayer];
  [registrar registerViewFactory:g3StreamOnlineFactory withId:@"g3_stream_online"];
  [registrar registerViewFactory:g3Factory withId:@"g3_stream"];
  [registrar registerViewFactory:factory withId:@"player"];
  [registrar addMethodCallDelegate:instance channel:channel];
}


- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"seekTo" isEqual:call.method]) {
      double position = [call.arguments[@"position" ] doubleValue];
      [self.mediaPlayer setPosition:position];
      result(nil);
  } else if([@"pauseVideo" isEqual:call.method]) {
      [self.mediaPlayer pause];
      result(nil);
  } else if([@"playVideo" isEqual:call.method]) {
      int retry = 3;
      if (![self.mediaPlayer isPlaying] && retry > 0) {
          [self.mediaPlayer play];
          retry--;
      }
      result(nil);
  } else if ([@"stopVideo" isEqual:call.method]) {
      [self.mediaPlayer stop];
      result(nil);
  } else if ([@"replay" isEqual:call.method]) {
      [self.mediaPlayer stop];
      [self.mediaPlayer setPosition:0.0];
      [self.mediaPlayer play];
      result(nil);
  } else if([@"getDuration" isEqual:call.method]) {
      VLCTime *time = self.mediaPlayer.media.length;
      if (time) {
          int duration = time.intValue / 1000; // Chuyển đổi thành giây
          result(@(duration));
      } else {
          result(@0); // Trả về 0 nếu không có thời lượng
      }
  } else if([@"getMetadataInStream" isEqual:call.method]) {
      result([metaDataInStream getMetaData]);
  } else if([@"playNextFileInStream" isEqual:call.method]) {
      NSString* path = call.arguments[@"path"];
      NSLog(@"Pathhhhhhhhhhhh%@:", path);
      [self playSegment:[NSURL fileURLWithPath:path]];
      result(nil);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

- (void)handleVideoEnd:(NSNotification *)notification {
    AVPlayerItem *endedItem = notification.object;
    if (endedItem == self.queuePlayer.currentItem) {
        [self.queuePlayer pause];
    }
}

- (void)playSegment:(NSURL *)fileURL {
    AVPlayerItem *playerItem = [AVPlayerItem playerItemWithURL:fileURL];
    [self.queuePlayer insertItem:playerItem afterItem:nil];
    [self.queuePlayer play];
}

@end
