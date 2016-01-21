//
//  ViewController.m
//  SampleMoPubNativeAd
//
//  Copyright © 2016 Zynga. All rights reserved.
//

#import "MPNativeAd.h"
#import "MPNativeAdConstants.h"
#import "MPNativeAdDelegate.h"
#import "MPNativeAdRendererConfiguration.h"
#import "MPNativeAdRequest.h"
#import "MPStaticNativeAdRenderer.h"
#import "MPStaticNativeAdRendererSettings.h"
#import "ViewController.h"

NSString *const kAdUnitID = @"11a17b188668469fb0412708c3d16813";

@interface ViewController () <MPNativeAdDelegate>

@property (nonatomic, weak) IBOutlet UILabel *titleLabel;
@property (nonatomic, weak) IBOutlet UILabel *textLabel;
@property (nonatomic, weak) IBOutlet UIImageView *mainImageView;
@property (nonatomic, weak) IBOutlet UIImageView *iconImageView;
@property (nonatomic, weak) IBOutlet UIButton *ctaButton;
@property (nonatomic, weak) IBOutlet UIButton *showAdButton;

@property (nonatomic, strong) MPNativeAd *response;

@end

@implementation ViewController

- (IBAction)showNativeAd:(id)sender {
    self.showAdButton.hidden = YES;

    MPStaticNativeAdRendererSettings *settings = [[MPStaticNativeAdRendererSettings alloc] init];
    MPNativeAdRendererConfiguration *config = [MPStaticNativeAdRenderer rendererConfigurationWithRendererSettings:settings];
    MPNativeAdRequest *adRequest = [MPNativeAdRequest requestWithAdUnitIdentifier:kAdUnitID rendererConfigurations:@[config]];
    [adRequest startWithCompletionHandler:^(MPNativeAdRequest *request, MPNativeAd *response, NSError *error) {
        if (!error) {
            NSDictionary *properties = response.properties;
            
            self.titleLabel.text = properties[kAdTitleKey];
            self.textLabel.text = properties[kAdTextKey];
            
            self.mainImageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:properties[kAdMainImageKey]]]];
            self.iconImageView.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:properties[kAdIconImageKey]]]];

            [self.ctaButton setTitle:properties[kAdCTATextKey] forState:UIControlStateNormal];
            self.ctaButton.hidden = NO;
            
            response.delegate = self;
            self.response = response;
            
            [self call:@"trackImpression" onNativeAd:response];
        }
    }];
}

- (IBAction)tap:(id)sender {
    self.ctaButton.enabled = NO;
    self.ctaButton.backgroundColor = [UIColor grayColor];
    [self call:@"displayAdContent" onNativeAd:self.response];
}

- (UIViewController *)viewControllerForPresentingModalView {
    return self;
}

- (void)call:(NSString *)name onNativeAd:(MPNativeAd *)nativeAd {
    Class nativeAdClass = NSClassFromString(@"MPNativeAd");
    SEL selector = NSSelectorFromString(name);
    if (selector != nil) {
        NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:[nativeAdClass instanceMethodSignatureForSelector:selector]];
        [invocation setSelector:selector];
        [invocation setTarget:nativeAd];
        [invocation performSelector:@selector(invoke) withObject:nil];
    }
}

@end
