'use client';
// TODO: this sgould be a template
import { useEffect, useState } from "react";
import { useSession} from "next-auth/react"
import { permanentRedirect } from 'next/navigation'
import Link from "next/link";
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
const AppsLayout = ({ children }: {
    children: React.ReactNode;
  }) => {
    const [ authenticated, setAuthenticated] = useState(false);
    const {data, status} = useSession();
    // console.log(">>>>>>integrator>AppsLayout...status, ", status);
    // console.log(">>>>integrator>>>AppsLayout...session, ", session);
    // console.log(">>>>>>>AppsLayout...status, ", status);
        
    useEffect(() => {
      console.log(">>>>>>integrator>AppsLayout...status, ", status);
      console.log(">>>>>>>AppsLayout...session, ", data);
      console.log(">>>>>>>AppsLayout...need sign in, ", data && data.user?.name && data?.user?.name.startsWith('camel_anon_'));
      if (data && data.user?.name && data?.user?.name.startsWith('camel_anon_')) {
        // router.push('/auth/login');
        console.log(">>>>>>>AppsLayout...permanentRedirect, ");
        // TODO: find better place to signout
        // signOut().then((data) => {
        //   // async sign-out returned
        //   console.log(">>>>>>>>>> Signed out anonymouse user");
        //   permanentRedirect('/auth/signin');
        // });
        permanentRedirect('/auth/signin');
        
      } else {
        setAuthenticated(true);
      }
      console.log(">>>>>>>AppsLayout...after redirect, ");
    }, [data, status]);
    
    return authenticated ? (
        <IntegratorConfigurationDataProvider>
            {children}
        </IntegratorConfigurationDataProvider>
    ) : (
     
        <Link href={`/auth/login`}>Log in</Link>
    );
}

export default AppsLayout
