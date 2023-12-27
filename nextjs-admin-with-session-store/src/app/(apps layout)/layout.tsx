
'use client'
import ProtectedSiteLayout from "@/layout/ProtectedSiteLayout";
//import { useSession } from "next-auth/react"
const AppsLayout = ({ children }) => {
    // client side only
    // const router = useRouter();

    return (
          <ProtectedSiteLayout>
            {children}
          </ProtectedSiteLayout>
    );
}

export default AppsLayout