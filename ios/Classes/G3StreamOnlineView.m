#import "G3StreamOnlineView.h"

@implementation G3StreamOnlineView {
    UIView *_view;
}
- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                         withQueuePlayer: (AVQueuePlayer*) queuePlayer
                         withPlayerLayer: (AVPlayerLayer*) playerLayer{
    if (self) {
        _view = [[UIView alloc] initWithFrame:frame];
        self.queuePlayer = queuePlayer;
        self.playerLayer = playerLayer;
        self.playerLayer.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.width*9/16);
        self.playerLayer.videoGravity = AVLayerVideoGravityResize;
        [_view.layer addSublayer:self.playerLayer];
    }
    return self;
}

- (UIView *)view {
    return _view;
}

-(void) dealloc {
    [self.queuePlayer pause];
}
@end
