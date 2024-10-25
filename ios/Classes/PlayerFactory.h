#import <Flutter/Flutter.h>

@interface PlayerFactory : NSObject <FlutterPlatformViewFactory>
- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger
                                  withMediaPlayer:(VLCMediaPlayer*) mediaPlayer;
@end