#import <Flutter/Flutter.h>
#import "MobileVLCKit/MobileVLCKit.h"
#import "AVFoundation/AVFoundation.h"

@interface DashcamPlayerPlugin : NSObject<FlutterPlugin>
@property (nonatomic, strong) VLCMediaPlayer *mediaPlayer;
@property (nonatomic, strong) AVQueuePlayer *queuePlayer;
@property (nonatomic, strong) AVPlayerLayer *playerLayer;

- (void)handleVideoEnd:(NSNotification *)notification;
@end
