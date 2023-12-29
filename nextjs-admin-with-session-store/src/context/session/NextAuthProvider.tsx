'use client';

import {SessionProvider} from 'next-auth/react';
import {ReactNode} from 'react';
import AnonymousSessionProvider from "@/context/session/AnonymousSessionProvider";

export default function NextAuthProvider({session,
    children
}: {
    session: any;
    children: ReactNode;
}) {
    
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
