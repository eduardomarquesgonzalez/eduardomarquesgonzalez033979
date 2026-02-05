/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        spotiplag: {
          black: "#000000",
          darkgray: "#121212",
          gray: "#181818",
          lightgray: "#282828",
          blue: "#1B3282",
          green: "#1DB954",
          bluehover: "#2542A9",
          white: "#FFFFFF",
          lightwhite: "#B3B3B3",
          darkwhite: "#535353",
        },
      },
      animation: {
        "slide-in-right": "slideInRight 0.3s ease-out",
      },
      keyframes: {
        slideInRight: {
          "0%": { transform: "translateX(100%)", opacity: "0" },
          "100%": { transform: "translateX(0)", opacity: "1" },
        },
      },
    },
  },
  plugins: [],
};
