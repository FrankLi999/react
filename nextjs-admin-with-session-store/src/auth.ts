import NextAuth, {User} from "next-auth"
import Okta from "next-auth/providers/okta";
import GitHubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials"
import type { NextAuthConfig } from "next-auth"
import { randomUUID } from 'crypto';
// import redis from '@/lib/redis';
// import { IORedisAdapter } from "@/lib/IORedisAdapter";
import { MongoDBAdapter } from "@auth/mongodb-adapter"
import clientPromise from "@/lib/mongodb"
import { logger } from "@/logger";
// import log4js from 'log4js';
// const log = log4js.getLogger("nextjs:api:auth");

export const config = {
  providers: [
    CredentialsProvider({
        name: "anonymous",
        credentials: {},
        async authorize(credentials, req) {
            return createAnonymousUser();
        },
    }),
    Okta({
      clientId: process.env.OKTA_OAUTH2_CLIENT_ID as string,
      clientSecret: process.env.OKTA_OAUTH2_CLIENT_SECRET as string,
      issuer: process.env.OKTA_OAUTH2_ISSUER as string,
    }),
    GitHubProvider({
      clientId: process.env.AUTH_GITHUB_ID as string,
      clientSecret: process.env.AUTH_GITHUB_SECRET as string,
    }),
  ],
  callbacks: {
      authorized({ request, auth }) {
        console.log(">>>>>>>>>>>authorized>>>req>>>", request);
        console.log(">>>>>>>>>>>authorized>>>auth>>>", auth);
        
        const { pathname } = request.nextUrl
        console.log(">>>>>>>>>>>authorized>>>pathname>>>", pathname);
        if (pathname === "/integrator/configuration-data") return !!auth
        return true
      },
      async redirect({ baseUrl }) {
        logger.info('winston redirect ...' + baseUrl);
        // log.info('log4js redirect ...' + baseUrl);
        return `${baseUrl}/integrator/configuration-data`;
      },
  },
  // adapter: IORedisAdapter(redis),
  adapter: MongoDBAdapter(clientPromise),
  // adapter: mongoAdapter,
  session: {
      // use default, an encrypted JWT (JWE) store in the session cookie
      strategy: "database",
      maxAge: 30 * 60
  },

} satisfies NextAuthConfig;

  // Helper functions

const createAnonymousUser = (): User => {
  // generate a random name and email for this anonymous user
  // const customConfig: Config = {
  //     dictionaries: [adjectives, colors, animals],
  //     separator: '-',
  //     length: 3,
  //     style: 'capital'
  // };
  // handle is simple-red-aardvark
  // const unique_handle: string = uniqueNamesGenerator(customConfig).replaceAll(' ','');
  // real name is Red Aardvark
  // const unique_realname: string = unique_handle.split('-').slice(1).join(' ');
  const unique_uuid: string = randomUUID();
  const unique_realname = `camel_anon_${unique_uuid}`;
  return {
      id: unique_uuid,
      email: `${unique_realname.toLowerCase()}@example.com`,
      name: unique_realname,
      image: ""
  };
};

export const { handlers, auth, signIn, signOut } = NextAuth(config)