import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// ★ 포인트: proxy.configure 로 Origin 헤더 제거
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5184,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
        ws: true,
        configure: (proxy /*, options*/) => {
          proxy.on("proxyReq", (proxyReq) => {
            // Spring 쪽 CORS를 우회하기 위해 Origin 제거
            try { proxyReq.removeHeader("origin"); } catch (_) {}
          });
        },
      },
    },
  },
});
