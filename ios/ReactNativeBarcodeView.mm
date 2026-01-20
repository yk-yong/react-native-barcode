#import "ReactNativeBarcodeView.h"

#import <CoreImage/CoreImage.h>
#import <React/RCTConversions.h>

#import <react/renderer/components/ReactNativeBarcodeViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/ReactNativeBarcodeViewSpec/EventEmitters.h>
#import <react/renderer/components/ReactNativeBarcodeViewSpec/Props.h>
#import <react/renderer/components/ReactNativeBarcodeViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"

using namespace facebook::react;

@implementation ReactNativeBarcodeView {
  UIImageView *_imageView;
  CGSize _lastRenderedSize;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
  return concreteComponentDescriptorProvider<
      ReactNativeBarcodeViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame {
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps =
        std::make_shared<const ReactNativeBarcodeViewProps>();
    _props = defaultProps;

    _imageView = [[UIImageView alloc] initWithFrame:self.bounds];
    _imageView.contentMode = UIViewContentModeScaleAspectFit;
    _imageView.clipsToBounds = YES;
    self.contentView = _imageView;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props
           oldProps:(Props::Shared const &)oldProps {
  const auto &oldViewProps =
      *std::static_pointer_cast<ReactNativeBarcodeViewProps const>(_props);
  const auto &newViewProps =
      *std::static_pointer_cast<ReactNativeBarcodeViewProps const>(props);

  bool shouldRender = false;

  if (oldViewProps.value != newViewProps.value) {
    shouldRender = true;
  }

  if (oldViewProps.format != newViewProps.format) {
    shouldRender = true;
  }

  if (oldViewProps.foregroundColor != newViewProps.foregroundColor ||
      oldViewProps.backgroundColor != newViewProps.backgroundColor) {
    shouldRender = true;
  }

  if (shouldRender) {
    [self renderBarcodeWithProps:newViewProps];
  }

  [super updateProps:props oldProps:oldProps];
}

- (void)layoutSubviews {
  [super layoutSubviews];
  if (!CGSizeEqualToSize(_lastRenderedSize, self.bounds.size)) {
    _lastRenderedSize = self.bounds.size;
    const auto &currentProps =
        *std::static_pointer_cast<ReactNativeBarcodeViewProps const>(_props);
    [self renderBarcodeWithProps:currentProps];
  }
}

- (void)renderBarcodeWithProps:(ReactNativeBarcodeViewProps const &)props {
  NSString *value = RCTNSStringFromString(props.value);
  if (value.length == 0) {
    _imageView.image = nil;
    return;
  }

  NSString *format = RCTNSStringFromString(props.format);
  if (format.length == 0) {
    format = @"qr";
  }

  UIColor *foregroundColor =
      props.foregroundColor ? RCTUIColorFromSharedColor(props.foregroundColor)
                            : [UIColor blackColor];
  UIColor *backgroundColor =
      props.backgroundColor ? RCTUIColorFromSharedColor(props.backgroundColor)
                            : [UIColor whiteColor];

  NSString *filterName = [self filterNameForFormat:format];
  if (filterName.length == 0) {
    [self
        emitErrorWithCode:@"unsupported_format"
                  message:[NSString stringWithFormat:@"Unsupported format: %@",
                                                     format]];
    _imageView.image = nil;
    return;
  }

  NSData *data = [value dataUsingEncoding:NSUTF8StringEncoding];
  CIFilter *generator = [CIFilter filterWithName:filterName];
  [generator setValue:data forKey:@"inputMessage"];

  if ([filterName isEqualToString:@"CIQRCodeGenerator"]) {
    [generator setValue:@"M" forKey:@"inputCorrectionLevel"];
  }

  CIImage *ciImage = generator.outputImage;
  if (ciImage == nil) {
    [self emitErrorWithCode:@"encode_failed"
                    message:@"Failed to encode barcode"];
    _imageView.image = nil;
    return;
  }

  CIFilter *colorFilter = [CIFilter filterWithName:@"CIFalseColor"];
  [colorFilter setValue:ciImage forKey:kCIInputImageKey];
  [colorFilter setValue:[[CIColor alloc] initWithColor:foregroundColor]
                 forKey:@"inputColor0"];
  [colorFilter setValue:[[CIColor alloc] initWithColor:backgroundColor]
                 forKey:@"inputColor1"];
  ciImage = colorFilter.outputImage ?: ciImage;

  CGRect extent = ciImage.extent;
  if (extent.size.width == 0 || extent.size.height == 0) {
    _imageView.image = nil;
    return;
  }

  CGSize targetSize = self.bounds.size;
  if (targetSize.width <= 0 || targetSize.height <= 0) {
    return;
  }

  CGFloat scale = MIN(targetSize.width / extent.size.width,
                      targetSize.height / extent.size.height);
  CIImage *scaledImage = [ciImage
      imageByApplyingTransform:CGAffineTransformMakeScale(scale, scale)];

  CIContext *context = [CIContext contextWithOptions:nil];
  CGImageRef cgImage = [context createCGImage:scaledImage
                                     fromRect:scaledImage.extent];
  if (cgImage == nil) {
    [self emitErrorWithCode:@"encode_failed"
                    message:@"Failed to render barcode"];
    _imageView.image = nil;
    return;
  }

  UIImage *image = [UIImage imageWithCGImage:cgImage
                                       scale:[UIScreen mainScreen].scale
                                 orientation:UIImageOrientationUp];
  CGImageRelease(cgImage);
  _imageView.image = image;
}

- (NSString *)filterNameForFormat:(NSString *)format {
  NSString *lowercase = [format lowercaseString];
  if ([lowercase isEqualToString:@"qr"]) {
    return @"CIQRCodeGenerator";
  }
  if ([lowercase isEqualToString:@"code128"]) {
    return @"CICode128BarcodeGenerator";
  }
  if ([lowercase isEqualToString:@"pdf417"]) {
    return @"CIPDF417BarcodeGenerator";
  }
  if ([lowercase isEqualToString:@"aztec"]) {
    return @"CIAztecCodeGenerator";
  }
  if ([lowercase isEqualToString:@"datamatrix"]) {
    return @"CIDataMatrixCodeGenerator";
  }
  return @"";
}

- (void)emitErrorWithCode:(NSString *)code message:(NSString *)message {
  auto eventEmitter =
      std::dynamic_pointer_cast<ReactNativeBarcodeViewEventEmitter const>(
          _eventEmitter);
  if (!eventEmitter) {
    return;
  }

  ReactNativeBarcodeViewEventEmitter::OnError event;
  event.message = message.UTF8String;
  if (code != nil) {
    event.code = code.UTF8String;
  }
  eventEmitter->onError(event);
}

@end
