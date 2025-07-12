import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css"; // ✅ CSS는 최상단에 import

import App from "./App.jsx"; // App import는 CSS 아래 있어도 무방

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);