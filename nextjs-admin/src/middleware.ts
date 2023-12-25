// export { default } from "next-auth/middleware";

import { withAuth } from "next-auth/middleware"

export default withAuth(
  // `withAuth` augments your `Request` with the user's token.
  function middleware(req) {
    // console.log(">>>>>>>>>>>>>>middleware>>>>>>,", req.nextauth);
    // console.log(">>>>>>>>>>>>>>middleware>>>>2>>,", req)
    console.log(req.nextauth.token)
  },
  {
    callbacks: {
      authorized: ({ token }) => {
        console.log(">>>>>>>>>>middleware>>>>>>>> token");
        return token && !token.name?.startsWith('camel_anon_')
      },
    },
  }
)


export const config = {
  matcher: ["/integrator/(.*)"],
};