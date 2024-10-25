#import <Flutter/Flutter.h>
#import <UIKit/UIKit.h>
#import <MobileVLCKit/MobileVLCKit.h>

@interface PlayerView : NSObject <FlutterPlatformView>
@property(nonatomic, strong) VLCMediaPlayer *mediaPlayer;

- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                    withMediaPlayer:(VLCMediaPlayer*) mediaPlayer;

- (UIView*_Nonnull)view;
@end
