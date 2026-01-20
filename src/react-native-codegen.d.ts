declare module 'react-native/Libraries/Types/CodegenTypes' {
  export type WithDefault<T, D> = T | D;
  export type DirectEventHandler<T> = (event: { nativeEvent: T }) => void;
  export type BubblingEventHandler<T> = (event: { nativeEvent: T }) => void;
  export type Int32 = number;
  export type Double = number;
  export type Float = number;
}
