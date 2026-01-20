# @yk-yong/react-native-barcode

Native barcode library for react-native

## Installation

```sh
npm install @yk-yong/react-native-barcode
```

## Usage

```js
import { ReactNativeBarcodeView } from '@yk-yong/react-native-barcode';

// ...

<ReactNativeBarcodeView
  value="https://example.com"
  format="qr"
  foregroundColor="#111111"
  backgroundColor="#FFFFFF"
  style={{ width: 180, height: 180 }}
/>;
```

## Props

| Prop              | Type                                                                                                                     | Required | Description                                                                             |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------ | -------- | --------------------------------------------------------------------------------------- |
| `value`           | `string`                                                                                                                 | ✅       | Data to encode in the barcode.                                                          |
| `format`          | `"qr" \| "code128" \| "code39" \| "ean13" \| "ean8" \| "upcA" \| "upcE" \| "itf" \| "pdf417" \| "aztec" \| "dataMatrix"` | ❌       | Barcode format. Defaults to `"qr"`.                                                     |
| `foregroundColor` | `ColorValue`                                                                                                             | ❌       | Foreground color for bars/modules. Defaults to black.                                   |
| `backgroundColor` | `ColorValue`                                                                                                             | ❌       | Background color for bars/modules. Defaults to white.                                   |
| `onError`         | `(event) => void`                                                                                                        | ❌       | Called when encoding fails. `event.nativeEvent` includes `message` and optional `code`. |

## Platform notes

- iOS uses CoreImage. Supported formats: `qr`, `code128`, `pdf417`, `aztec`, `dataMatrix`.
- Android uses ZXing and supports all formats listed above.

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
