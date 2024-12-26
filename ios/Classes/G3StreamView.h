#import <Flutter/Flutter.h>
#import <UIKit/UIKit.h>
#import "zlib.h"
#import "SharedMetaData.h"

@interface G3StreamView : NSObject <FlutterPlatformView>
@property (nonatomic, strong) NSURLSessionWebSocketTask *websocketTask;
@property (nonatomic, strong) NSMutableArray* frameQueue;
@property (nonatomic, strong) NSString* metaData;
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UILabel *metaDataLabel;
@property (nonatomic, assign) BOOL isStreaming;
@property (nonatomic, strong) dispatch_queue_t processingQueue;
@property (nonatomic, strong) SharedMetaData* metaDataInStream;

- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                          metaDaInStream:(SharedMetaData * _Nonnull)metaDataInStream;

- (UIView*_Nonnull)view;
- (void)dispose;
@end
