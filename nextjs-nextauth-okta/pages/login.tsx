import { signIn } from "next-auth/react"
export default function Login() {
    return (
        <>
            Not Logged In <button onClick={() => signIn('okta')}>Sign in</button>
        </>
    )
}