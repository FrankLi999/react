'use client';

import {SessionProvider} from 'next-auth/react';
import {ReactNode} from 'react';
import AnonymousSessionProvider from "@/context/session/AnonymousSessionProvider";

export default function NextAuthProvider({session,
    children
}: {
    children: ReactNode;
}) {
    console.log(">>>>>>>>>>>NextAuthProvider>>>>>", session);
    return (
        <>
            <SessionProvider session={session}>
                <AnonymousSessionProvider>
                    {children}
                </AnonymousSessionProvider>    
            </SessionProvider>
        </>
    );
}
