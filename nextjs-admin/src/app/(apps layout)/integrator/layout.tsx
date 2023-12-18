'use client';
// TODO: this sgould be a template
import Link from "next/link";
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';


const AppsLayout = ({ children }) => {
    let authenticated = true;
    return authenticated ? (
        <IntegratorConfigurationDataProvider>
            {children}
        </IntegratorConfigurationDataProvider>
    ) : (
     
        <Link href={`/auth/login`}>Log in</Link>
    );
}

export default AppsLayout