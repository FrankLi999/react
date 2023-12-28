https://github.com/decovicdev/next-auth-database-session
https://github.com/nextauthjs/next-auth/discussions/4394
nextjs oauth agent:
  https://github.com/curityio/oauth-agent-node-nextjs

  POST /login/start	Start a login by providing the request URL to the SPA and setting temporary cookies.
  POST /login/end	Complete a login and issuing secure cookies for the SPA containing encrypted tokens.
  GET /userInfo	Return information from the User Info endpoint for the SPA to display.
  GET /claims	Return ID token claims such as auth_time and acr.
  POST /refresh	Refresh an access token and rewrite cookies.
  POST /logout	Clear cookies and return an end session request URL.


  Strong browser security with HTTP only and SameSite=strict cookies

nextauth-redis
https://github.com/nextauthjs/next-auth/blob/main/packages/adapter-upstash-redis/src/index.ts

Please Follow below URL for detail online documentation  
    https://next-nubra-ui.vercel.app/  


  https://github.com/weehongayden/nextjs-app-router-nextauth/tree/main      
This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

## Getting Started

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/basic-features/font-optimization) to automatically optimize and load Inter, a custom Google Font.

## Learn More

To learn more about Next.js, take a look at the following resources:

- [Next.js Documentation](https://nextjs.org/docs) - learn about Next.js features and API.
- [Learn Next.js](https://nextjs.org/learn) - an interactive Next.js tutorial.

You can check out [the Next.js GitHub repository](https://github.com/vercel/next.js/) - your feedback and contributions are welcome!

## Deploy on Vercel

The easiest way to deploy your Next.js app is to use the [Vercel Platform](https://vercel.com/new?utm_medium=default-template&filter=next.js&utm_source=create-next-app&utm_campaign=create-next-app-readme) from the creators of Next.js.

Check out our [Next.js deployment documentation](https://nextjs.org/docs/deployment) for more details.
