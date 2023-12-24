'use client';
// TODO: this sgould be a template
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
import { useSession } from 'next-auth/react';

const AppsLayout = ({ children }) => {
    const {data: session, status} = useSession();
    console.log(">>>>>>>AppsLayout...status, ", status);
    console.log(">>>>>>>AppsLayout...session, ", session);
    
    return (
        <IntegratorConfigurationDataProvider>
            {children}
        </IntegratorConfigurationDataProvider>
    );
}

export default AppsLayout