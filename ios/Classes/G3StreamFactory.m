#import "G3StreamFactory.h"

@implementation G3StreamFactory {
    NSObject<FlutterBinaryMessenger>* _messenger;
}

- (instancetype)initWithMessenger:(NSObject<FlutterBinaryMessenger>*)messenger
                                  {
  self = [super init];
  if (self) {
    _messenger = messenger;
      _views = [NSMutableDictionary dictionary];
  }
  return self;
}

- (NSObject<FlutterPlatformView>*)createWithFrame:(CGRect)frame
                                   viewIdentifier:(int64_t)viewId
                                        arguments:(id _Nullable)args {
  G3StreamView *view = [[G3StreamView alloc] initWithFrame:frame
                              viewIdentifier:viewId
                                   arguments:args
                             binaryMessenger:_messenger];
    self.views[@(viewId)] = view;
    return view;
}

/// Implementing this method is only necessary when the `arguments` in `createWithFrame` is not `nil`.
- (NSObject<FlutterMessageCodec>*)createArgsCodec {
    return [FlutterStandardMessageCodec sharedInstance];
}

- (void)disposeViewWithId:(int64_t)viewId {
    G3StreamView *view = self.views[@(viewId)];
    [view dispose];
    [self.views removeObjectForKey:@(viewId)];
}

@end
