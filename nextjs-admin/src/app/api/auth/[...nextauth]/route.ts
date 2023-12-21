// import { PrismaAdapter } from "@next-auth/prisma-adapter";
// import { PrismaClient } from "@prisma/client";
import NextAuth from "next-auth";

import Okta from "next-auth/providers/okta";
import GitHubProvider from "next-auth/providers/github";
// import EmailProvider from "next-auth/providers/email";
// import GoogleProvider from "next-auth/providers/google";
// const prisma = new PrismaClient();

const handler = NextAuth({
  providers: [
    Okta({
      clientId: process.env.OKTA_OAUTH2_CLIENT_ID as string,
      clientSecret: process.env.OKTA_OAUTH2_CLIENT_SECRET as string,
      issuer: process.env.OKTA_OAUTH2_ISSUER as string,
    }),
    GitHubProvider({
      clientId: process.env.AUTH_GITHUB_ID as string,
      clientSecret: process.env.AUTH_GITHUB_SECRET as string,
    }),
    
    // GoogleProvider({
    //   clientId: process.env.GOOGLE_CLIENT_ID,
    //   clientSecret: process.env.GOOGLE_CLIENT_SECRET,
    // }),
    // EmailProvider({
    //   server: {
    //     host: process.env.EMAIL_SERVER_HOST,
    //     port: process.env.EMAIL_SERVER_PORT,
    //     auth: {
    //       user: process.env.EMAIL_SERVER_USER,
    //       pass: process.env.EMAIL_SERVER_PASSWORD,
    //     },
    //   },
    //   from: process.env.EMAIL_FROM,
    // }),
  ],
  // adapter: PrismaAdapter(prisma),
  pages: {
    signIn: "/auth/signin",
  },
  callbacks: {
    async signIn(userDetail) {
      if (Object.keys(userDetail).length === 0) {
        return false;
      }
      return true;
    },
    async redirect({ baseUrl }) {
      return `${baseUrl}/integrator`;
      // return `integrator/configuration-data`;
    },
  },
  secret: process.env.NEXTAUTH_SECRET as string,
});

export { handler as GET, handler as POST };