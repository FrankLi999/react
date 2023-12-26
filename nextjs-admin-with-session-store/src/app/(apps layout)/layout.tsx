
'use client'
import ProtectedSiteLayout from "@/layout/ProtectedSiteLayout";
import { permanentRedirect, useRouter } from 'next/navigation'
import { useSession } from "next-auth/react"
import { useEffect } from "react";
const AppsLayout = ({ children }) => {
    // client side only
    // const router = useRouter();
    const {data: session, status} = useSession();
    console.log(">>>>>>>AppsLayout...status, ", status);
        
    useEffect(() => {
      console.log(">>>>>>>AppsLayout...session, ", session);
      console.log(">>>>>>>AppsLayout...need sign in, ", !session || session?.user?.name && session?.user?.name.startsWith('anon_'));
      if (status !== 'loading' && (!session || session?.user?.name && session?.user?.name.startsWith('anon_'))) {
        // router.push('/auth/login');
        console.log(">>>>>>>AppsLayout...permanentRedirect, ");
        permanentRedirect('/auth/signin');
      }
      console.log(">>>>>>>AppsLayout...after redirect, ");
    }, [status]);
    
    return (
          <ProtectedSiteLayout>
            {children}
          </ProtectedSiteLayout>
    );
}

export default AppsLayout