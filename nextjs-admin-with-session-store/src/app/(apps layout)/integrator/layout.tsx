'use client';
// TODO: this sgould be a template
import { useEffect } from "react";
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
import { useSession} from "next-auth/react"
import { permanentRedirect } from 'next/navigation'

const AppsLayout = ({ children }) => {
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
        
      }
      console.log(">>>>>>>AppsLayout...after redirect, ");
    }, [status]);
    
    
    if (data && data.user?.name && data?.user?.name.startsWith('camel_anon_')) {
      return (<div>Need log in</div>);
    } else {
      return (<IntegratorConfigurationDataProvider>
        {children}
        </IntegratorConfigurationDataProvider>);
    }
}

export default AppsLayout