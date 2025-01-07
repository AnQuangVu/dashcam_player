#import "G3StreamOnlineFactory.h"

@implementation G3StreamOnlineFactory {
    NSObject<FlutterBinaryMessenger>* _messenger;
}

- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger
                      withQueuePlayer: (AVQueuePlayer*) queuePlayer
                      withPlayerLayer: (AVPlayerLayer*) playerLayer{
  self = [super init];
  if (self) {
      _queuePlayer = queuePlayer;
      _playerLayer = playerLayer;
  }
  return self;
}

- (NSObject<FlutterPlatformView>*)createWithFrame:(CGRect)frame
                                   viewIdentifier:(int64_t)viewId
                                        arguments:(id _Nullable)args {
  G3StreamOnlineView *view = [[G3StreamOnlineView alloc] initWithFrame:frame
                              viewIdentifier:viewId
                                   arguments:args
                                           binaryMessenger:_messenger
                                            withQueuePlayer: (AVQueuePlayer*) _queuePlayer
                                            withPlayerLayer: (AVPlayerLayer*) _playerLayer];
    return view;
}
@end
