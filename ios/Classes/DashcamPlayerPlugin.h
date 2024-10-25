#import <Flutter/Flutter.h>
#import "MobileVLCKit/MobileVLCKit.h"

@interface DashcamPlayerPlugin : NSObject<FlutterPlugin>
@property (nonatomic, strong) VLCMediaPlayer *mediaPlayer;
@end
