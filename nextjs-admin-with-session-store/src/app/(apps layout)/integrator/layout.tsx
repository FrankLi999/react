'use client';
// TODO: this sgould be a template
import { useEffect } from "react";
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
import { useSession } from 'next-auth/react';
import { permanentRedirect } from 'next/navigation'
const AppsLayout = ({ children }) => {
    const {data, status} = useSession();
    // console.log(">>>>>>integrator>AppsLayout...status, ", status);
    // console.log(">>>>integrator>>>AppsLayout...session, ", session);
    // console.log(">>>>>>>AppsLayout...status, ", status);
        
    useEffect(() => {
      console.log(">>>>>>integrator>AppsLayout...status, ", status);
      console.log(">>>>>>>AppsLayout...session, ", data);
      console.log(">>>>>>>AppsLayout...need sign in, ", data && data.user?.name && data?.user?.name.startsWith('anon_'));
      if (data && data.user?.name && data?.user?.name.startsWith('anon_')) {
        // router.push('/auth/login');
        console.log(">>>>>>>AppsLayout...permanentRedirect, ");
        // permanentRedirect('/auth/signin');
      }
      console.log(">>>>>>>AppsLayout...after redirect, ");
    }, [status]);
    
    return (
        <IntegratorConfigurationDataProvider>
            {children}
        </IntegratorConfigurationDataProvider>
    );
}

export default AppsLayout