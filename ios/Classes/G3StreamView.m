#import "G3StreamView.h"

@implementation G3StreamView {
    UIView *_view;
}
- (instancetype _Nullable )initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger {
    if (self) {
        self.imageView = [[UIImageView alloc] initWithFrame:frame];
        self.imageView.contentMode = UIViewContentModeScaleAspectFit;
        _view = self.imageView;
        [self initView];
        self.processingQueue = dispatch_queue_create("frameProcessingQueue",  DISPATCH_QUEUE_SERIAL);
        self.frameQueue = [NSMutableArray array];
        [self startSocket];
        [self displayFrame];
    }
    return self;
}

-(void)initView{
    // LinearLayout equivalent
    UIView *linearLayout = [[UIView alloc] init];
    linearLayout.translatesAutoresizingMaskIntoConstraints = NO;
    linearLayout.backgroundColor = [UIColor clearColor];
    [_view addSubview:linearLayout];

    // Set layout constraints for linearLayout
    [NSLayoutConstraint activateConstraints:@[
        [linearLayout.trailingAnchor constraintEqualToAnchor:_view.trailingAnchor],
        [linearLayout.bottomAnchor constraintEqualToAnchor:_view.bottomAnchor],
        [linearLayout.widthAnchor constraintEqualToAnchor:_view.widthAnchor],
        [linearLayout.heightAnchor constraintEqualToAnchor:_view.heightAnchor]
    ]];

     //Add ImageView (logo)
    UIImage* image = [UIImage imageNamed:@"logo_white.png"];
    UIImageView *logoImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"logo_white.png"]];
    logoImageView.translatesAutoresizingMaskIntoConstraints = NO;
    [linearLayout addSubview:logoImageView];

    // Add layout constraints for logoImageView
    CGFloat screenWidth = [UIScreen mainScreen].bounds.size.width;
    [NSLayoutConstraint activateConstraints:@[
        [logoImageView.trailingAnchor constraintEqualToAnchor:linearLayout.trailingAnchor constant:-screenWidth*0.37],
        [logoImageView.bottomAnchor constraintEqualToAnchor:linearLayout.bottomAnchor constant:-3]
    ]];

    // Add TextView (meta_data equivalent)
    self.metaDataLabel = [[UILabel alloc] init];
    self.metaDataLabel.translatesAutoresizingMaskIntoConstraints = NO;
    self.metaDataLabel.font = [UIFont systemFontOfSize:8];
    self.metaDataLabel.textColor = [UIColor whiteColor];
    [linearLayout addSubview:self.metaDataLabel];

    // Add layout constraints for metaDataLabel
    [NSLayoutConstraint activateConstraints:@[
        [self.metaDataLabel.trailingAnchor constraintEqualToAnchor:linearLayout.trailingAnchor constant:-20],
        [self.metaDataLabel.bottomAnchor constraintEqualToAnchor:linearLayout.bottomAnchor constant:-3]
    ]];
}

-(void) displayFrame {
    dispatch_async(self.processingQueue, ^{
        while (true) {
            NSData *data = nil;
            @synchronized (self.frameQueue) {
                if (self.frameQueue.count > 0) {
                    data = self.frameQueue.firstObject;
                    [self.frameQueue removeObjectAtIndex:0];
                    if (self.frameQueue.count > 50) {
                        [self.frameQueue removeObjectAtIndex:0];
                    }
                }
            }
            if (data) {
                @autoreleasepool {
                    [self processFrameData:data];
                }
            }
            if(!self.isStreaming)break;
        }
    });
}

-(void)startSocket {
    NSURL *url = [NSURL URLWithString:@"ws://192.168.43.1:9090"];
    self.websocketTask = [[NSURLSession sharedSession] webSocketTaskWithURL:url];
    [self.websocketTask resume];
    self.isStreaming = YES;
    [self receiveFrame];
}

-(void)receiveFrame {
    __weak typeof(self) weakSelf = self;
    [self.websocketTask receiveMessageWithCompletionHandler:^(NSURLSessionWebSocketMessage * _Nullable message, NSError * _Nullable error) {
        __strong typeof(self) strongSelf = weakSelf;
        if(!strongSelf) return;
        
        if(error) {
            NSLog(@"Websocket Error: %@", error);
            strongSelf.isStreaming = NO;
            return;
        }
        if (message.type == NSURLSessionWebSocketMessageTypeData) {
            [self.frameQueue addObject:message.data];
        } else if (message.type == NSURLSessionWebSocketMessageTypeString) {
            self.metaData = message.string;
        }
        
        if (strongSelf.isStreaming) {
            [
                strongSelf receiveFrame
            ];
        }
    }];
}

-(void) processFrameData:(NSData*) data {
    UIImage *image = [UIImage imageWithData:[self decompressData:data]];
    if(image) {
        dispatch_async(dispatch_get_main_queue(), ^{
            self.metaDataLabel.text = self.metaData;
            self.imageView.image = image;
        });
    }
}

- (NSData*) decompressData: (NSData*) compressData {
    NSUInteger bufferSize = 1024;
    NSMutableData *decompressData = [NSMutableData data];
    
    z_stream stream;
    stream.zalloc = Z_NULL;
    stream.zfree = Z_NULL;
    stream.opaque = Z_NULL;
    stream.avail_in = (uint)compressData.length;
    stream.next_in = (Bytef*)compressData.bytes;
    if (inflateInit(&stream) != Z_OK) {
        return nil;
    }
    Bytef *outBuffer = malloc(bufferSize);
    if(!outBuffer) {
        inflateEnd(&stream);
        return nil;
    }
    
    do {
        stream.next_out = outBuffer;
        stream.avail_out = (uint)bufferSize;
        
        int status = inflate(&stream, Z_SYNC_FLUSH);
        if (status == Z_STREAM_ERROR || status == Z_DATA_ERROR || status == Z_MEM_ERROR) {
            free(outBuffer);
            inflateEnd(&stream);
            return nil;
        }
        [decompressData appendBytes:outBuffer length:(bufferSize - stream.avail_out)];
    } while (stream.avail_out == 0);
    
    free(outBuffer);
    inflateEnd(&stream);
    
    return decompressData;
}

- (UIView *)view {
    return _view;
}

- (void)dispose {
    self.isStreaming = NO;
    [self.websocketTask cancelWithCloseCode:NSURLSessionWebSocketCloseCodeGoingAway reason:nil];
}

- (void)dealloc {
    self.isStreaming = NO;
    [self.websocketTask cancelWithCloseCode:NSURLSessionWebSocketCloseCodeGoingAway reason:nil];
}
@end
