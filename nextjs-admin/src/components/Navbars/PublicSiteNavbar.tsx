import React from "react";
import Link from "next/link";
// import { Link, useLocation } from "react-router-dom";
import { useRouter, usePathname } from 'next/navigation'
import {
    Badge,
    Button,
    Card,
    Navbar,
    Nav,
    Container,
    Col,
} from "react-bootstrap";
function PublicSiteNavbar() {
    const [collapseOpen, setCollapseOpen] = React.useState(false);
    // const location = useLocation();
    const pathname = usePathname();
    const router = useRouter();
    return (
        <>
            <Navbar className="sb-topnav navbar navbar-expand navbar-dark bg-dark" variant="dark" expand="lg">
                <Container>
                    <div className="navbar-wrapper">
                        <Navbar.Brand href="#pablo" onClick={(e: { preventDefault: () => void; }) => e.preventDefault()}>
                            <span className="d-none d-md-block">Integrator Dashboard</span>
                            <span className="d-block d-md-none">Integrator Dashboard</span>
                        </Navbar.Brand>
                    </div>
                    <button
                        className="navbar-toggler navbar-toggler-right border-0"
                        type="button"
                        onClick={() => setCollapseOpen(!collapseOpen)}
                    >
                        <span className="navbar-toggler-bar burger-lines"></span>
                        <span className="navbar-toggler-bar burger-lines"></span>
                        <span className="navbar-toggler-bar burger-lines"></span>
                    </button>
                    <Navbar.Collapse className="justify-content-end" in={collapseOpen}>
                        <Nav navbar>
                            <Nav.Item
                                className={
                                    pathname === "/auth/signin"
                                        ? "active mr-1"
                                        : "mr-1"
                                }
                            >
                                <Nav.Link href={`/auth/signinn`} as={Link}>
                                    <i className="nc-icon nc-mobile"></i>
                                    Login
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item
                                className={
                                    pathname === "/auth/lock-screen"
                                        ? "active mr-1"
                                        : "mr-1"
                                }
                            >
                                <Nav.Link href={`/auth/lock-screen`} as={Link}>
                                    <i className="nc-icon nc-key-25"></i>
                                    Lock
                                </Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>                                
        </>
    );
}
export default PublicSiteNavbar;