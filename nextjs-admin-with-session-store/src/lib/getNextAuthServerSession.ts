import { getServerSession } from "next-auth/next";
import { cookies, headers } from "next/headers";

import { authOptions} from "@/app/api/auth/[...nextauth]/route";

export default async function getNextAuthServerSession() {
  const session = await getServerSession(
    authOptions(
      {
        headers: headers(),
        cookies: cookies(),
      } as any,
      { params: { nextauth: ["session"] } }
    )
  );
  console.log("get server session>>>>>>>>>>>>>>>>>>>>", session);
  return session;
}
