'use client'
import PublicSiteLayout from "@/layout/PublicSiteLayout";

const AuthLayout = (children) => {
    return (
        <PublicSiteLayout>
            {children}
        </PublicSiteLayout>
    );
};
export default AuthLayout;