/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        text_color: "rgba(0, 0, 0, 0.8)",
        button_color: "#49E4FA",
        button_hover_color: "rgba(73, 228, 250, 0.8)",
        background_color: "#795FFF",
        background_color_hover: "rgba(121, 95, 255, 0.8)",
      },
    },
  },
  plugins: [],
};
