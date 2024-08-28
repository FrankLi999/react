import React from "react";
import {Link} from "react-router-dom";
import { useTranslation } from 'react-i18next';
import {
    Button,
    Dropdown,
    Navbar,
    Nav,
} from "react-bootstrap";

function ProtectedSiteNavbar() {
    const { t, i18n } = useTranslation();

    const changeLanguage = (lng) => {
      i18n.changeLanguage(lng);
    };
    
    return (
        <>
            <Navbar className="sb-topnav navbar navbar-expand navbar-dark bg-dark" style={{ paddingRight: '5em' }} variant="dark" expand="lg">
                <Link to="/" className="navbar-brand">Camel Dashboard</Link>
                <Button className="btn btn-link btn-sm order-1 order-lg-0" id="sidebarToggle" onClick={() => document.body.classList.toggle("sb-sidenav-toggled")}>
                    <i className="fas fa-bars"></i>
                </Button>
                <Navbar.Collapse className="justify-content-end">
                    <Nav navbar>
                        <Dropdown as={Nav.Item}>
                            <Dropdown.Toggle
                                as={Nav.Link}
                                id="dropdown-41471887334"
                                variant="default"
                            >
                                <i className="fas fa-globe"></i>
                            </Dropdown.Toggle>
                            <Dropdown.Menu                                
                                aria-labelledby="navbarDropdownMenuLink"
                            ><Dropdown.Item
                                    onClick={() => changeLanguage('en')}
                                >
                                    <i className="fas fa-flag"></i>
                                    <span className="ml-1">English</span>
                                </Dropdown.Item>
                                <div className="divider"></div>
                                <Dropdown.Item
                                    onClick={() => changeLanguage('fr')}
                                >
                                    <i className="fas fa-language"></i>
                                    <span className="ml-1">French</span>
                                </Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                        <Dropdown as={Nav.Item}>
                            <Dropdown.Toggle
                                as={Nav.Link}
                                id="dropdown-41471887333"
                                variant="default"
                            >
                                <i className="fas fa-list"></i>
                            </Dropdown.Toggle>
                            // alignRight
                            <Dropdown.Menu                                
                                aria-labelledby="navbarDropdownMenuLink"
                            ><Dropdown.Item
                                    href="/"
                                    onClick={(e) => e.preventDefault()}
                                >
                                    <i className="fas fa-lock"></i>
                                    <span className="ml-1">Lock Screen</span>
                                </Dropdown.Item>
                                <div className="divider"></div>
                                <Dropdown.Item
                                    href="/"
                                    onClick={(e) => e.preventDefault()}
                                >
                                    <i className="fas fa-bars"></i>
                                    <span className="ml-1">Logout</span>
                                </Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                        
                    </Nav>
                </Navbar.Collapse>
                
            </Navbar>
        </>
    );
}
export default ProtectedSiteNavbar;