import {
  codegenNativeComponent,
  type ColorValue,
  type ViewProps,
} from 'react-native';
import type {
  DirectEventHandler,
  WithDefault,
} from 'react-native/Libraries/Types/CodegenTypes';

export type BarcodeFormat =
  | 'qr'
  | 'code128'
  | 'code39'
  | 'ean13'
  | 'ean8'
  | 'upcA'
  | 'upcE'
  | 'itf'
  | 'pdf417'
  | 'aztec'
  | 'dataMatrix';

export type BarcodeErrorEvent = {
  message: string;
  code?: string;
};

export interface ReactNativeBarcodeViewProps extends ViewProps {
  value: string;
  format?: WithDefault<BarcodeFormat, 'qr'>;
  foregroundColor?: ColorValue;
  backgroundColor?: ColorValue;
  onError?: DirectEventHandler<BarcodeErrorEvent>;
}

export default codegenNativeComponent<ReactNativeBarcodeViewProps>(
  'ReactNativeBarcodeView'
);
