import { getServerSession } from "next-auth/next";
import { cookies, headers } from "next/headers";

import { authOptions} from "@/app/api/auth/[...nextauth]/route";

export default async function getNextAuthServerSession() {
  const naheaders = headers();
  const nacookies = cookies();
  console.log(">>>>>getNextAuthServerSession cookies>>>>>>>>>>>>", nacookies);
  console.log(">>>>>getNextAuthServerSession headers>>>>>>>>>>>>", naheaders);
  const session = await getServerSession(
    authOptions(
      {
        headers: naheaders,
        cookies: nacookies
      } as any,
      { params: { nextauth: ["session"] } }
    )
  );
  console.log("get server session>>>>>>>>>>>>>>>>>>>>", session);
  return session;
}
