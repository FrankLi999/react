import { getServerSession } from "next-auth";

export async function GET() {
  console.log('>>>>>>>>>>>protected ...');
  const session = await getServerSession();
  console.log('>>>>>>>>>>>protected .session..', session);
  if (!session) {
    return new Response("Unauthorized", {
      status: 401,
    });
  }

  return new Response("Hello, Next.js!", {
    status: 200,
  });
}
