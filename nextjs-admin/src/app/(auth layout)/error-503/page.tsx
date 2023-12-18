"use client"
import Link from "next/link";
import { Button } from "react-bootstrap";

const Error503 = () => {
    return (
        <div className="not-found-wrap text-center">
            <h1 className="text-60">404</h1>
            <p className="text-36 subheading mb-3">Error!</p>
            <p className="mb-5  text-muted text-18">
            Server is temporarily unable to handle the request. This may be due to the server being overloaded or down for maintenance.
            </p>
            <Link href={`/`} className="btn btn-lg btn-primary btn-rounded">
                Go back to home
            </Link>
            <br/>
            <Button variant="primary" className="mt-4" as={Link} href="/" >Return to Home</Button>
        </div>
    );
};

export default Error503