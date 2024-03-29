'use client';

import {SessionProvider} from 'next-auth/react';
import {ReactNode} from 'react';
import AnonymousSessionProvider from "@/context/session/AnonymousSessionProvider";

export default function NextAuthProvider({
    children
}: {
    children: ReactNode;
}) {
    return (
        <>
            <SessionProvider>
                <AnonymousSessionProvider>
                    {children}
                </AnonymousSessionProvider>
            </SessionProvider>
        </>
    );
}
