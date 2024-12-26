#import "SharedMetaData.h"

@implementation SharedMetaData
-(void) setMetaData:(NSString *)metaData {
    _metaData = metaData;
}

-(NSString*_Nullable)getMetaData {
    return self.metaData;
}
@end
