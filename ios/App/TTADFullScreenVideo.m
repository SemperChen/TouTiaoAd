//
//  RNBUADFullScreenVideo.m
//  TTAd
//
//  Created by BeatYourself on 2019/9/4.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTUtils.h>

#import "TTADFullScreenVideo.h"
#import "AppDelegate.h"

static NSString *const kEventAdShowed = @"fullScreenVideoAdShowed";
static NSString *const kEventAdClick = @"fullScreenVideoAdClick";
static NSString *const kEventAdClose = @"fullScreenVideoAdClose";
static NSString *const kEventAdComplete = @"fullScreenVideoComplete";
static NSString *const kEventAdSkipped = @"fullScreenVideoSkipped";
static NSString *const kEventAdFailedToLoad = @"fullScreenVideoAdFailedToLoad";

@implementation TTADFullScreenVideo
{
  BOOL hasListeners;
}

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

- (void)startObserving
{
  hasListeners = YES;
}

- (void)stopObserving
{
  hasListeners = NO;
}

RCT_EXPORT_MODULE(TTAdFullScreenVideo);
RCT_EXPORT_METHOD(showFullScreenVideoAd:(NSString *)slotID){
  self.fullscreenVideoAd = [[BUFullscreenVideoAd alloc] initWithSlotID:slotID];
  self.fullscreenVideoAd.delegate = self;
  [self.fullscreenVideoAd loadAdData];
  
}
- (NSArray<NSString *> *)supportedEvents
{
  return @[
           kEventAdClick,
           kEventAdClose,
           kEventAdShowed,
           kEventAdSkipped,
           kEventAdComplete,
           kEventAdFailedToLoad
           ];
}

- (void)fullscreenVideoAdVideoDataDidLoad:(BUFullscreenVideoAd *)fullscreenVideoAd{
  dispatch_async(dispatch_get_main_queue(), ^{
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [self.fullscreenVideoAd showAdFromRootViewController:app.window.rootViewController];
    
  });
}
- (void)fullscreenVideoAdDidVisible:(BUFullscreenVideoAd *)fullscreenVideoAd{
  if (hasListeners){
    [self sendEventWithName:kEventAdShowed body:nil];
  }
}
- (void)fullscreenVideoAdDidClick:(BUFullscreenVideoAd *)fullscreenVideoAd{
  if (hasListeners){
    [self sendEventWithName:kEventAdClick body:nil];
  }
}
- (void)fullscreenVideoAdDidClose:(BUFullscreenVideoAd *)fullscreenVideoAd{
  if (hasListeners){
    [self sendEventWithName:kEventAdClose body:nil];
  }
}
- (void)fullscreenVideoAdDidPlayFinish:(BUFullscreenVideoAd *)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error{
  if (hasListeners){
    [self sendEventWithName:kEventAdComplete body:nil];
  }
}
- (void)fullscreenVideoAdDidClickSkip:(BUFullscreenVideoAd *)fullscreenVideoAd{
  if (hasListeners){
    [self sendEventWithName:kEventAdSkipped body:nil];
  }
}
- (void)fullscreenVideoAd:(BUFullscreenVideoAd *)fullscreenVideoAd didFailWithError:(NSError *_Nullable)error{
  if (hasListeners) {
    NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError([NSString stringWithFormat: @"%ld", (long)[error code]], error.localizedDescription, error);
    [self sendEventWithName:kEventAdFailedToLoad body:jsError];
  }
}

@end
