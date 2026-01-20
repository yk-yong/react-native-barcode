import { View, StyleSheet, Text } from 'react-native';
import { ReactNativeBarcodeView } from '@yk-yong/react-native-barcode';

export default function App() {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>QR</Text>
      <ReactNativeBarcodeView
        value="https://example.com"
        format="qr"
        foregroundColor="#111111"
        backgroundColor="#FFFFFF"
        style={styles.qr}
      />
      <Text style={styles.label}>Code128</Text>
      <ReactNativeBarcodeView
        value="123456789012"
        format="code128"
        foregroundColor="#111111"
        backgroundColor="#FFFFFF"
        style={styles.barcode}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#F4F5F7',
    padding: 24,
  },
  label: {
    fontSize: 14,
    fontWeight: '600',
    marginTop: 16,
    marginBottom: 8,
  },
  qr: {
    width: 180,
    height: 180,
  },
  barcode: {
    width: 260,
    height: 80,
  },
});
