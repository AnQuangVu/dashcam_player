#import "PlayerView.h"
#import "PlayerFactory.h"

@implementation PlayerFactory {
    NSObject<FlutterBinaryMessenger>* _messenger;
    VLCMediaPlayer* _mediaPlayer;
}

- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger
                                  withMediaPlayer:(VLCMediaPlayer*) mediaPlayer {
  self = [super init];
  if (self) {
    _messenger = messenger;
    _mediaPlayer = mediaPlayer;
  }
  return self;
}

- (NSObject<FlutterPlatformView>*)createWithFrame:(CGRect)frame
                                   viewIdentifier:(int64_t)viewId
                                        arguments:(id _Nullable)args {
  return [[PlayerView alloc] initWithFrame:frame
                              viewIdentifier:viewId
                                   arguments:args
                             binaryMessenger:_messenger
                             withMediaPlayer:_mediaPlayer];
}

/// Implementing this method is only necessary when the `arguments` in `createWithFrame` is not `nil`.
- (NSObject<FlutterMessageCodec>*)createArgsCodec {
    return [FlutterStandardMessageCodec sharedInstance];
}

@end
