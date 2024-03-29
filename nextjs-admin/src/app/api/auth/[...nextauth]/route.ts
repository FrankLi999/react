// https://github.com/quanhua92/next-auth-ioredis-adapter-example
import NextAuth, {Account, Profile, AuthOptions, SessionStrategy, Awaitable, Session, User} from "next-auth"
import Okta from "next-auth/providers/okta";
import GitHubProvider from "next-auth/providers/github";
import CredentialsProvider from "next-auth/providers/credentials"
import {JWT} from "next-auth/jwt";
import {AdapterUser} from "next-auth/adapters";
// import redis from '@/lib/redis';
// import { IORedisAdapter } from "@/lib/IORedisAdapter";
import { logger } from "@/logger";
import log4js from 'log4js';
// import { uniqueNamesGenerator, Config, adjectives, colors, starWars, animals } from 'unique-names-generator';
import { randomUUID } from 'crypto';
// import Debug from 'debug';
// const debug = Debug("nextjs:api:auth");
// import { MongoDBAdapter } from "@auth/mongodb-adapter"
// import clientPromise from "@/lib/mongodb"
// const mongoAdapter = MongoDBAdapter(clientPromise);
// const redisAdapter = IORedisAdapter(redis);
const log = log4js.getLogger("nextjs:api:auth");
// log.level = "debug";
export const authOptions: AuthOptions = {
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
        async jwt({token, account, profile}: {token: JWT, account: Account | null, profile?: Profile}): Promise<JWT> {
            logger.info("winston >>>>>>>>>> jwt token," );
            // logger.info("winston >>>>>>>>>> jwt account," + account);
            // logger.info("winston >>>>>>>>>> jwt profile," + profile);


            log.info("log4js >>>>>>>>>> jwt token,");
            // log.info("log4js >>>>>>>>>> jwt account," + account);
            // log.info("log4js >>>>>>>>>> jwt profile," + profile);
            console.log(token, account, profile);
            if (account && account?.expires_at && account?.type === 'oauth') {
                // at sign-in, persist in the JWT the GitHub account details to enable brokered requests in the future
                token.access_token = account.access_token;
                token.expires_at = account.expires_at;
                token.refresh_token = account.refresh_token;
                token.refresh_token_expires_in = account.refresh_token_expires_in;
                token.provider = 'github';
            }
            if (!token.provider) token.provider = token.name?.startsWith('camel_anon_') ? 'anonymous' : 'next_auth';
            token.role = token.provider === 'anonymous' ? 'anon' : 'authenticated'
            return token;
        },
        async session({session, token, user}: {session: Session, token: JWT, user: AdapterUser}): Promise<Session> {
            // don't make the token (JWT) contents available to the client session (JWT), but flag that they're server-side
            logger.info("winston >>>>>>>>>> session token,");
            // logger.info("winston >>>>>>>>>> session user," + user);
            // logger.info("winston >>>>>>>>>> session session," + session);

            log.info("log4js >>>>>>>>>> session token,");
            // log.info("log4js >>>>>>>>>> session user," + user);
            // log.info("log4js >>>>>>>>>> session session," + session);
            console.log(token, session, user);

            if (token?.provider) {
                session.token_provider = token?.provider;
            } else if (token.name && !token.name.startsWith('camel_anon_')) {
                session.token_provider = 'next_auth';
            }
            // session.user.id = user.id;
            // session.user.role = token.name && !token.name.startsWith('camel_anon_') ? (user.role || "authenticated") : 'anon'; 
            return session;
        },
        async signIn(userDetail) {
          logger.info('winston >>> userDetail ....' + userDetail);
          log.info('log4js >>> userDetail ....' + userDetail);
          if (Object.keys(userDetail).length === 0) {
            return false;
          }
          return true;
        },
        async redirect({ baseUrl }) {
          logger.info('winston redirect ...' + baseUrl);
          log.info('log4js redirect ...' + baseUrl);
          return `${baseUrl}/integrator/configuration-data`;
        },
    },
    events: {
        async signIn({user, account, profile}: {user: User, account: Account | null, profile?: Profile}): Promise<void> {
            log.debug(`log4js  signIn of ${user.name} from ${user?.provider || account?.provider}`);
        },
        async signOut({session, token}: {session: Session, token: JWT}): Promise<void> {
            log.debug(`log4js  signOut of ${token.name} from ${token.provider}`);
        },
    },
    // adapter: redisAdapter,
    // adapter: mongoAdapter,
    session: {
        // use default, an encrypted JWT (JWE) store in the session cookie
        strategy: "jwt" as SessionStrategy,
        maxAge: 30 * 60
    },
    pages: {
      signIn: "/auth/signin",
    },

}
const handler = NextAuth(authOptions);

export {handler as GET, handler as POST}

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
        image: "",
        provider: "anonymous"
    };
};