// Polyfill para compatibilidade com sockjs-client
import { Buffer } from "buffer";

if (typeof window !== "undefined") {
  window.global = window;
  window.Buffer = Buffer;
  window.process = {
    env: { DEBUG: undefined },
  } as any;
}
