'use client';
// TODO: this sgould be a template
import Link from "next/link";
import ProtectedSiteLayout from "@/layout/ProtectedSiteLayout";

const AppsLayout = ({ children }) => {
    let authenticated = true;
    return authenticated ? (
        <ProtectedSiteLayout>
            {children}
        </ProtectedSiteLayout>
    ) : (
     
        <Link href={`/auth/login`}>Log in</Link>
    );
}

export default AppsLayout