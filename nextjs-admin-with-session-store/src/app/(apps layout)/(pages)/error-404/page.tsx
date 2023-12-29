import Link from "next/link";

const PagNotFoundError = () => {
    return (
        <div className="not-found-wrap text-center">
            <h1 className="text-60">404</h1>
            <p className="text-36 subheading mb-3">Error!</p>
            <p className="mb-5  text-muted text-18">
                {`The page you were looking for doesn't exist.`}
            </p>
            <Link href={`/`} className="btn btn-lg btn-primary btn-rounded">
                Go back to home
            </Link>
        </div>
    );
};

export default PagNotFoundError;

