#import "PlayerView.h"

@implementation PlayerView {
    UIView *_view;
}
- (instancetype _Nullable )initWithFrame:(CGRect)frame
                          viewIdentifier:(int64_t)viewId
                               arguments:(id _Nullable)args
                         binaryMessenger:(NSObject<FlutterBinaryMessenger>*_Nonnull)messenger
                         withMediaPlayer:(VLCMediaPlayer*) mediaPlayer {
    if (self) {
        //[mediaPlayer stop];
        _view = [[UIView alloc] initWithFrame:frame];
        _view.backgroundColor = [UIColor blackColor];
        mediaPlayer.drawable = _view;

        [self setupControls];

        NSString *videoURL = args[@"videoURL"];
        NSNumber *isLocalFile = args[@"isLocalFile"];
        BOOL isLocal = [isLocalFile boolValue];
        VLCMedia *media;
        if (isLocal) {
            media = [VLCMedia mediaWithPath:videoURL];
        } else {
            media = [VLCMedia mediaWithURL:[NSURL URLWithString: videoURL]];
        }
        //[media setLength:[VLCTime timeWithNumber:@(60 * 1000)]];
        mediaPlayer.media = media;
        [mediaPlayer play];
        [self startProgressUpdater];
    }
    return self;
}

- (void)setupControls {
    CGFloat buttonSize = 50.0;
    CGFloat padding = 10.0;

    // Play/Pause button
    self.playPauseButton = [[UIButton alloc] initWithFrame:CGRectMake(padding, self.view.frame.size.height - buttonSize - padding, buttonSize, buttonSize)];
    [self.playPauseButton setImage:[UIImage systemImageNamed:@"play.fill"] forState:UIControlStateNormal];
    [self.playPauseButton addTarget:self action:@selector(togglePlayPause) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.playPauseButton];

    // Progress slider
    self.progressSlider = [[UISlider alloc] initWithFrame:CGRectMake(padding * 2 + buttonSize, self.view.frame.size.height - buttonSize, self.view.frame.size.width - buttonSize - padding * 3, 20)];
    [self.progressSlider addTarget:self action:@selector(progressSliderChanged:) forControlEvents:UIControlEventValueChanged];
    [self.view addSubview:self.progressSlider];

    // Current time label
    self.currentTimeLabel = [[UILabel alloc] initWithFrame:CGRectMake(padding, self.view.frame.size.height - buttonSize - 20, 60, 20)];
    self.currentTimeLabel.textColor = [UIColor whiteColor];
    self.currentTimeLabel.font = [UIFont systemFontOfSize:12];
    self.currentTimeLabel.text = @"00:00";
    [self.view addSubview:self.currentTimeLabel];

    // Duration label
    self.durationLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.view.frame.size.width - 60 - padding, self.view.frame.size.height - buttonSize - 20, 60, 20)];
    self.durationLabel.textColor = [UIColor whiteColor];
    self.durationLabel.font = [UIFont systemFontOfSize:12];
    self.durationLabel.text = @"00:00";
    [self.view addSubview:self.durationLabel];
}

- (void)togglePlayPause {
    if (self.mediaPlayer.isPlaying) {
        [self.mediaPlayer pause];
        [self.playPauseButton setImage:[UIImage systemImageNamed:@"play.fill"] forState:UIControlStateNormal];
    } else {
        [self.mediaPlayer play];
        [self.playPauseButton setImage:[UIImage systemImageNamed:@"pause.fill"] forState:UIControlStateNormal];
    }
}

- (void)progressSliderChanged:(UISlider *)slider {
    NSInteger newTime = slider.value * self.mediaPlayer.media.length.intValue / 100;
    [self.mediaPlayer setTime:[VLCTime timeWithNumber:@(newTime)]];
}

- (void)startProgressUpdater {
    self.progressTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updateProgress) userInfo:nil repeats:YES];
}

- (void)updateProgress {
    VLCTime *currentTime = self.mediaPlayer.time;
    VLCTime *durationTime = self.mediaPlayer.media.length;

    self.currentTimeLabel.text = [currentTime stringValue];
    self.durationLabel.text = [durationTime stringValue];

    if (durationTime.intValue > 0) {
        self.progressSlider.value = currentTime.intValue * 100.0 / durationTime.intValue;
    } else {
        self.progressSlider.value = 0;
    }
}

- (UIView *)view {
    return _view;
}
@end
