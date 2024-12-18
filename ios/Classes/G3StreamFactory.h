#import "G3StreamView.h"
#import <Flutter/Flutter.h>

@interface G3StreamFactory : NSObject <FlutterPlatformViewFactory>
@property (nonatomic, strong) NSMutableDictionary<NSNumber *, G3StreamView *> *views;
- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger;
@end
