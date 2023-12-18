/** @type {import('next').NextConfig} */
module.exports = {
  async redirects() {
    return [
      {
      source: '/',
      destination: '/integrator/configuration-data',
      permanent: true,
      },
    ]
   },
   async rewrites() {
    return [
      {
        source: "/integrator/api/configurations",
        destination: "http://localhost:8080/api/configurations",
      }, {
        source: "/integrator/api/imports",
        destination: "http://localhost:8080/api/imports",
      }
    ]
  },

}

