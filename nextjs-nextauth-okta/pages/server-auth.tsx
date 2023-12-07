import { unstable_getServerSession } from "next-auth/next"
import { authOptions } from "./api/auth/[...nextauth]"

export async function getServerSideProps(context: any) {
    const session = await unstable_getServerSession(
        context.req, context.res, authOptions)

    if (session) {
        console.log(">>>>>>>session?.user?.name>>>", session?.user?.name);
        return {
            props: { username: session?.user?.name }
        }
    }
    return {
        redirect: { destination: "/login", permanent: false }
    }
}

export default function ServerSideAuth(props: any) {
    return (
        <>
            <h1>Protected Page</h1>
            <p>You can view this page because you are signed in as {props.username}</p>
        </>
    )
}