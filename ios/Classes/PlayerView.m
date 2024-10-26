#import "PlayerView.h"

@implementation PlayerView {
    UIView *_view;
}
- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                    withMediaPlayer:(VLCMediaPlayer*) mediaPlayer {
    if (self) {
        //[mediaPlayer stop];
        _view = [[UIView alloc] initWithFrame:frame];
        _view.backgroundColor = [UIColor blackColor];
        mediaPlayer.drawable = _view;
        NSString *videoURL = args[@"videoURL"];
        NSNumber *isLocalFile = args[@"isLocalFile"];
        BOOL isLocal = [isLocalFile boolValue];
        VLCMedia *media;
        if (isLocal) {
            media = [VLCMedia mediaWithPath:videoURL];
        } else {
            media = [VLCMedia mediaWithURL:[NSURL URLWithString: videoURL]];
        }
        //[media setLength:[VLCTime timeWithNumber:@(60 * 1000)]];
        mediaPlayer.media = media;
        [mediaPlayer play];
    }
    return self;
}

- (UIView *)view {
    return _view;
}
@end
