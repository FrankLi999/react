'use client';

import ProtectedSiteLayout from "@/layout/ProtectedSiteLayout";
import { permanentRedirect, useRouter } from 'next/navigation'
import { useSession } from "next-auth/react"

const AppsLayout = ({ children }) => {
    // client side only
    // const router = useRouter();
    const {data: session, status} = useSession();
    console.log(">>>>>>>AppsLayout...status, ", status);
    console.log(">>>>>>>AppsLayout...session, ", session);
    // if (status === "unauthenticated" || session?.token_provider === 'anonymous') {
    //     // router.push('/auth/login');
    //     permanentRedirect('/auth/signin');
    // }
    console.log(">>>>>>>AppsLayout...after redirect, ");
    return (
          <ProtectedSiteLayout>
            {children}
          </ProtectedSiteLayout>
    );
}

export default AppsLayout