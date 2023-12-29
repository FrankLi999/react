import {signIn, signOut, useSession} from "next-auth/react"
import SessionPoke from "@/components/account/SessionPoke";
import * as React from "react";

export default function Account() {
    const {data} = useSession();
    const sign_in_element = <button onClick={() => signIn("github")}>Sign in</button>;
    const sign_out_element = <button onClick={() => signOut()}>Sign out</button>;
    const sign_element = (data && data.user?.name && data?.user?.name.startsWith('camel_anon_')) ? sign_in_element : sign_out_element;
    console.log(">>>>>>>>> account, ", data);
    return (
        <>
            <SessionPoke></SessionPoke>
            {sign_element}
        </>
    );
}
