import {useEffect, ReactNode} from "react";
import {signIn, useSession} from "next-auth/react"

export default function AnonymousSessionProvider({
    children
}: {
    children: ReactNode
}) {
    const {data, status} = useSession();
    
    useEffect(() => {
        
        console.log("<<<<<<<<<<anon provider >>status>", status);
        
        if (status === "unauthenticated") {
            // login as anonymous            
            console.log("<<<<<<<<<<anon provider >unauthenticated>***>");
            console.log("<<<<<<<<<<anon provider >session>>", data);
            // await signIn("credentials");
            signIn("credentials")
                .then((data) => {
                    // async sign-in returned
            });
        }
    }, [data, status]);
    return (
        <>
            {children}
        </>
    );
}