import {signIn, signOut, useSession} from "next-auth/react"
import styles from "./Account.module.css"
import SessionPoke from "@/components/account/SessionPoke";
import * as React from "react";

export default function Account() {
    const {data: session, status} = useSession();
    const sign_in_element = <button onClick={() => signIn("github")}>Sign in</button>;
    const sign_out_element = <button onClick={() => signOut()}>Sign out</button>;
    const sign_element = ((status === "authenticated") && session?.token_provider !== 'anonymous') ? sign_out_element : sign_in_element;
    console.log(">>>>>>>>> account, ", session);
    return (
        <>
            <SessionPoke></SessionPoke>
            {sign_element}
        </>
    );
}
