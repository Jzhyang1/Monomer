/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        primary: ["Inter", "sans-serif"],
        inter: ["Inter", "sans-serif"],
        poppins: ["Poppins", "sans-serif"],
        palanquin: ["Palanquin", "sans-serif"],
        heebo: ["Heebo", "sans-serif"],
        maven: ["Maven Pro", "sans-serif"],
        caveat: ["Caveat", "cursive"],
      },
    },
  },
  plugins: [],
};
