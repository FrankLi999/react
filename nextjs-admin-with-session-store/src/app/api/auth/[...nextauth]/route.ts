// https://github.com/quanhua92/next-auth-ioredis-adapter-example
import NextAuth, {Account, Profile, AuthOptions, Session, User } from "next-auth"
import * as headers from "next/headers";
import Okta from "next-auth/providers/okta";
import GitHubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials"
import { JWT, encode, decode } from "next-auth/jwt"
import redis from '@/lib/redis';
import { IORedisAdapter } from "@/lib/IORedisAdapter";
import { logger } from "@/logger";
import log4js from 'log4js';
import { randomUUID } from 'crypto';
// import { MongoDBAdapter } from "@auth/mongodb-adapter"
// import clientPromise from "@/lib/mongodb"
import { Adapter } from "next-auth/adapters";
import { NextRequest } from "next/server";
const log = log4js.getLogger("nextjs:api:auth");

const session = {
    // strategy: "database",
    maxAge: 30 * 24 * 60 * 60, // 30 days
    updateAge: 24 * 60 * 60, // 24 hours
  }
interface RouteHandlerContext {
  params: { nextauth: string[] }
}

export const authOptions = (request: NextRequest, context: RouteHandlerContext): AuthOptions => {
  const adapter: Adapter = IORedisAdapter(redis);
  const { params } = context;
  console.log(">>>>>>>>>>>>authOptions>>>>>", params, request.method);
  const isCredentialsCallback =
    params?.nextauth?.includes("callback") &&
    params.nextauth.includes("credentials") &&
    request.method === "POST";
  const isGetCallback =
    params?.nextauth?.includes("callback") &&
    request.method === "GET";
  const isCallback =
    params?.nextauth?.includes("callback") &&
    request.method === "POST";
  console.log(">>>>>>>>>>>>authOptions>>>isCredentialsCallback>>", isCredentialsCallback);
  console.log(">>>>>>>>>>>>authOptions>>>isGetCallback>>", isGetCallback);
  console.log(">>>>>>>>>>>>authOptions>>>isCallback>>", isCallback);
  return {
    providers: [
      CredentialsProvider({
          name: "anonymousCedentials",
          credentials: {},
          async authorize(credentials, req) {
              return await createAnonymousUser(request, adapter);
          },
      }),
      Okta({
        clientId: process.env['OKTA_OAUTH2_CLIENT_ID'] as string,
        clientSecret: process.env['OKTA_OAUTH2_CLIENT_SECRET'] as string,
        issuer: process.env['OKTA_OAUTH2_ISSUER'] as string,
      }),
      GitHubProvider({
        clientId: process.env['AUTH_GITHUB_ID'] as string,
        clientSecret: process.env['AUTH_GITHUB_SECRET'] as string,
      }),
    ],
    callbacks: {
      async signIn({user}) {
        logger.info('winston >>> user signin ....' + user);
        log.info('log4js >>> user signin ....' + user);
        console.log(">>>>>>>>>sign in user>", user);

        // const isCredentialsCallback =
        //   req.query?.nextauth?.includes("callback") &&
        //   req.query?.nextauth?.includes("credentials") &&
        //   req.method === "POST";
        console.log(">>>>>>>>>sign in req query param>", params);
        console.log(">>>>>>>>>sign in req isCredentialsCallback>", isCredentialsCallback);
        console.log(">>>>>>>>>sign in req isGetCallback>", isGetCallback);
        console.log(">>>>>>>>>>>>authOptions>>>isCallback>>", isCallback);
        if (isCredentialsCallback) {
          const sessionToken = randomUUID()
          const sessionExpiry = new Date(Date.now() + session.maxAge * 1000)
          const userSession = {
            sessionToken : sessionToken,
            userId: user.id,
            user : JSON.stringify({
                name : user.name,
                email : user.email
            }),
            expires: sessionExpiry,
            // userAgent: req.headers["user-agent"] ?? null,
          };
          console.log(">>>>>>>>> will create user session>>>>", userSession);
          adapter.createSession && await adapter.createSession(userSession);
          // console.log(">>>>>>>>> user session created>>>>", s);
          headers.cookies().set("next-auth.session-token", sessionToken, {
              expires: sessionExpiry,
          });
          // console.log(">>>>>>>>> cookie created>>>>", cookies().get("next-auth.session-token"));
        } else if (isGetCallback) {
          // TODO: only do it for anon session
          console.log("sign in none credential >>>>>>session>>")
          const currentSessionToken = headers.cookies().get("next-auth.session-token")?.value;
          console.log("delete session >>>>>>>>", currentSessionToken);
          if (currentSessionToken) {
            headers.cookies().delete("next-auth.session-token");
            console.log("deleted session cookie >>will deleteSession>>>>>>", currentSessionToken);
            adapter.deleteSession && await adapter.deleteSession(currentSessionToken);
            console.log("deleted session >>>>>>>>", currentSessionToken);
          }
        } else {
          console.log(">>>>>>>>>>> github signin user", user);
          console.log(">>>>>>>>>>> github signin isGetCallback", isGetCallback);
          console.log(">>>>>>>>>>> github signin isGetCallback", headers.cookies());
        }
        return true;
      },
      async redirect({url, baseUrl }) {
        logger.info('winston redirect ...' + baseUrl);
        log.info('log4js redirect ...' + `${baseUrl}/integrator/configuration-data`);
        // return `${baseUrl}/integrator/configuration-data`;
        return `${baseUrl}/home`;
        // return baseUrl;
      },
    },
    jwt: {
      maxAge: 60 * 60 * 24 * 30,
      encode: async (arg) => {
        // const isCredentialsCallback =
        //   req.query?.nextauth?.includes("callback") &&
        //   req.query?.nextauth?.includes("credentials") &&
        //   req.method === "POST";
        console.log(">>>>>>>>>jwt encode req query>", params);
        if (isCredentialsCallback) {
          console.log(">>>>>>>>>>> jwt encode >>isCredentialsCallback>>>", isCredentialsCallback);
          const cookie = headers.cookies().get("next-auth.session-token");
          console.log(">>>>>>>>>>> jwt encode cookie", cookie?.value);
          if (cookie) return cookie.value;
          return "";
        }

        const jwt = encode(arg);
        console.log(">>>>>>>>>jwt encoded jwt>", jwt);
        return jwt;
      },
      decode: async (arg) => {
        // const isCredentialsCallback =
        //   req.query?.nextauth?.includes("callback") &&
        //   req.query?.nextauth?.includes("credentials") &&
        //   req.method === "POST";
        console.log(">>>>>>>>>jwt decode req query>", params);
        if (isCredentialsCallback) {
          console.log(">>>>>>>>>>> jwt decode >>isCredentialsCallback>>>", isCredentialsCallback);
          return null;
        }
        const decoded = decode(arg);
        console.log(">>>>>>>>>jwt encoded decoded>", decoded);
        return decoded;
      },
    },
    events: {
      async signIn({user, account, profile}: {user: User, account: Account | null, profile?: Profile}): Promise<void> {
          // log.debug(`log4js  signIn of ${user?.name} from ${user?.provider || account?.provider}`);
          log.debug(`log4js  signIn of ${user?.name}`);
      },
      async signOut({session, token}: {session: Session, token: JWT}): Promise<void> {
          log.debug(`log4js  signOut of ${token?.name} from ${token?.provider}`);
          console.log("sign out >>>>>>session>>", session)
          const { sessionToken = "" } = session as unknown as {
            sessionToken?: string;
          };
          console.log("sign out >>>>>>>>", sessionToken);
          if (sessionToken) {
            adapter.deleteSession && await adapter.deleteSession(sessionToken);
          }
      },
    },
    adapter,
    session,
    pages: {
      signIn: "/auth/signin",
    },
    debug: process.env['NODE_ENV'] === "development",
    secret: process.env['NEXTAUTH_SECRET'],
  }
};

const handler = async (request: NextRequest, context: RouteHandlerContext) => {
  console.log(">>>>>>>>>>> handler context>>>", context);
  return await NextAuth(request, context, authOptions(request, context));
}

// const handler = async (req: interface RouteHandlerContext {
// , res: NextApiResponse) => {
//    return await NextAuth(req, res, authOptions(req, res));
// }

export {handler as GET, handler as POST}

// Helper functions

const createAnonymousUser = async (request: NextRequest, adapter: Adapter): Promise<User> => {
    console.log(">>>>>>>>>createAnonymousUser>>>");
    const unique_uuid: string = randomUUID();
    const unique_realname = `camel_anon_${unique_uuid}`;

    const user: User = {
        id: unique_uuid,
        email: `${unique_realname.toLowerCase()}@example.com`,
        name: unique_realname,
        image: "",
        // provider: "anonymous"
    };
    return user;
};
