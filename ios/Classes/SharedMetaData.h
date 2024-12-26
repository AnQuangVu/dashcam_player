#import <Flutter/Flutter.h>
#import <UIKit/UIKit.h>

@interface SharedMetaData : NSObject
@property (nonatomic, strong) NSString* _Nullable metaData;
-(void)setMetaData:(NSString*_Nullable) metaData;
-(NSString*_Nullable)getMetaData;
@end
