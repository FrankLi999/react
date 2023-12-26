import {useEffect, ReactNode} from "react";
import {signIn, useSession} from "next-auth/react"

export default function AnonymousSessionProvider({
    children
}: {
    children: ReactNode
}) {
    const {data: session, status} = useSession();
    console.log("<<<<<<<<<<anon provider >sessopm>>", session);
    console.log("<<<<<<<<<<anon provider >>status>", status);
    console.log(">>>>>>>anon provider...need sign in, ", session === undefined || session?.user?.name && session?.user?.name.startsWith('anon_'));
    /*
    useEffect(() => {
        if (status === "unauthenticated") {
            // login as anonymous
            signIn("credentials")
                .then((data) => {
                    // async sign-in returned
                });
        }
    }, [status]);
    */
    return (
        <>
            {children}
        </>
    );
}