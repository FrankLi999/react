
import React from "react";
import { Link, useLocation } from "react-router-dom";
import ProtectedSiteNavbar from "../Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../Footers/ProtectedSiteFooter";
import {
    Badge,
    Button,
    ButtonGroup,
    Card,
    Collapse,
    Form,
    InputGroup,
    Navbar,
    Nav,
    Pagination,
    Container,
    Row,
    Col
} from "react-bootstrap";
const Sidebar = () => {
    // to check for active links and opened collapses
    let location = useLocation();
    // this is for the user collapse
    const [checkoutCollapseState, setCheckoutCollapseState] = React.useState(false);
    const activeRoute = (path: any) => {
        return location.pathname === path ? "active" : "";
    };
    return (
        <>
            <nav className="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                <div className="sb-sidenav-menu">
                    <Nav as="ul" className="nav">
                        <div className="sb-sidenav-menu-heading">Integrator Services</div>
                        <Nav.Item
                            className={checkoutCollapseState ? "active" : ""}
                            as="li"
                            key="checkout"
                        >
                            <Nav.Link
                                className={checkoutCollapseState ? "nav-link": "nav-link collapsed"}
                                data-toggle="collapse"
                                onClick={(e:any) => {
                                    e.preventDefault();
                                    setCheckoutCollapseState(!checkoutCollapseState);
                                }}
                                aria-expanded={checkoutCollapseState}
                            >
                                <div className="sb-nav-link-icon"><i className="fas fa-book-open"></i></div>
                                Checkout
                                <div className="sb-sidenav-collapse-arrow"><i className="fas fa-angle-down"></i></div>
                            </Nav.Link>
                            <Collapse in={checkoutCollapseState}>
                                <div>
                                    <Nav as="ul" className="sb-sidenav-menu-nested nav accordion">
                                        <Nav.Item
                                            className={activeRoute("/integrator/configuration-data")}
                                            as="li"
                                            key="ConfigurationData"
                                        >
                                            <Nav.Link to="/integrator/configuration-data" as={Link} className="nav-link collapsed">
                                                <>
                                                    <p>Configuration Data</p>
                                                </>
                                            </Nav.Link>
                                        </Nav.Item>
                                        <Nav.Item
                                            className={activeRoute("/integrator/refresh-configuration")}
                                            as="li"
                                            key="RefreshConfiguration"
                                        >
                                            <Nav.Link to="/integrator/refresh-configuration" as={Link} className="nav-link collapsed">
                                                <>
                                                    <p>Refresh Configuration</p>
                                                </>
                                            </Nav.Link>
                                        </Nav.Item>
                                    </Nav>
                                </div>
                            </Collapse>
                        </Nav.Item>
                    </Nav>
                </div>
                <div className="sb-sidenav-footer">
                    <div className="small">Logged in as:</div>
                    Camel
                </div>
            </nav>        
        </>
    );
}
export default Sidebar;