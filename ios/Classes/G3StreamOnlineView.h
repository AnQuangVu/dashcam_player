#import <Flutter/Flutter.h>
#import <UIKit/UIKit.h>
#import <AVKit/AVKit.h>

@interface G3StreamOnlineView : NSObject <FlutterPlatformView>
@property (nonatomic, strong) AVQueuePlayer *queuePlayer;
@property (nonatomic, strong) AVPlayerLayer *playerLayer;
@property (strong, nonatomic) AVPlayerViewController *playerViewController;
- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                         withQueuePlayer: (AVQueuePlayer*) queuePlayer
                         withPlayerLayer: (AVPlayerLayer*) playerLayer;

- (UIView*_Nonnull)view;
- (void)dispose;
@end
