/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#2F5664",   // 진한 청록 계열
        secondary: "#3B7F91", // 조금 밝은 블루
      },
    },
  },
  plugins: [],
};

