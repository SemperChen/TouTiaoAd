//
//  RNBUADFullScreenVideo.h
//  TTAd
//
//  Created by BeatYourself on 2019/9/4.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#import <BUAdSDK/BUFullscreenVideoAd.h>

@interface TTADFullScreenVideo : RCTEventEmitter<RCTBridgeModule,BUFullscreenVideoAdDelegate>
@property (nonatomic, strong) BUFullscreenVideoAd *fullscreenVideoAd;

@end
