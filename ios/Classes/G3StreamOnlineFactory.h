#import "G3StreamOnlineView.h"
#import <Flutter/Flutter.h>
#import "AVFoundation/AVFoundation.h"

@interface G3StreamOnlineFactory : NSObject <FlutterPlatformViewFactory>
@property (nonatomic, strong) AVQueuePlayer *queuePlayer;
@property (nonatomic, strong) AVPlayerLayer *playerLayer;
- (instancetype _Nullable )initWithMessenger:(NSObject<FlutterBinaryMessenger>*_Nullable)messenger
                             withQueuePlayer: (AVQueuePlayer*) queuePlayer
                             withPlayerLayer: (AVPlayerLayer*) playerLayer;
@end
