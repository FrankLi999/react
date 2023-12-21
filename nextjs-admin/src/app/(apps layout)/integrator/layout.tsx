'use client';
// TODO: this sgould be a template
import Link from "next/link";
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';
import AuthSessionProvider from "@/context/session/AuthSessionProvider";


const AppsLayout = ({ children }) => {
    let authenticated = true;
    return authenticated ? (
        <AuthSessionProvider>
            <IntegratorConfigurationDataProvider>
                {children}
            </IntegratorConfigurationDataProvider>
        </AuthSessionProvider>
    ) : (
     
        <Link href={`/auth/login`}>Log in</Link>
    );
}

export default AppsLayout