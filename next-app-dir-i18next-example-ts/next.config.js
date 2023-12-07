/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true, // Recommended for the `pages` directory, default in `app`.
  swcMinify: true,
  // experimental: {
  //   // Required: for next 13
  //   appDir: true
  // }
}
console.log(">>>>XXXX env", process.env.NEXT_PUBLIC_XXX);
module.exports = nextConfig
