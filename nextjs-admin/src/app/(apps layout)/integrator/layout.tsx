'use client';
// TODO: this sgould be a template
import { IntegratorConfigurationDataProvider } from '@/context/integrator-configuration/IntegratorConfigurationDataProvider';

const AppsLayout = ({ children }) => {
    
    return (
        <IntegratorConfigurationDataProvider>
            {children}
        </IntegratorConfigurationDataProvider>
    );
}

export default AppsLayout