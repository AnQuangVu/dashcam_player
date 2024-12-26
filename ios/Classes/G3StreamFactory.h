#import "G3StreamView.h"
#import <Flutter/Flutter.h>

@interface G3StreamFactory : NSObject <FlutterPlatformViewFactory>
@property (nonatomic, strong) NSMutableDictionary<NSNumber *, G3StreamView *> * _Nullable views;
@property (nonatomic, strong) SharedMetaData  * _Nullable metaDataInStream;
- (instancetype _Nullable )initWithMessenger:(NSObject<FlutterBinaryMessenger>*_Nullable)messenger
                   metaDaInStream: (SharedMetaData* _Nonnull)metaDataInStream;
@end
