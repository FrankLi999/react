'use client';

import ProtectedSiteLayout from "@/layout/ProtectedSiteLayout";

const AppsLayout = ({ children }: {
    children: React.ReactNode;
  }) => {
    return (
          <ProtectedSiteLayout>
            {children}
          </ProtectedSiteLayout>
    );
}

export default AppsLayout
