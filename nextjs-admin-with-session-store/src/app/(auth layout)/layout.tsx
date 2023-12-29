'use client'
import PublicSiteLayout from "@/layout/PublicSiteLayout";

const AuthLayout = ({children}: {
  children: React.ReactNode;
}) => {
    return (
        <PublicSiteLayout>
            {children}
        </PublicSiteLayout>
    );
};
export default AuthLayout;
