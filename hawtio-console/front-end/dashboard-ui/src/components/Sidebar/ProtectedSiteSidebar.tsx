
import React from "react";
import { Link, useLocation } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import {
    Collapse,
    Nav
} from "react-bootstrap";
import routes from "../../routes";
const ProtectedSiteSidebar = () => {
    // to check for active links and opened collapses
    let location = useLocation();
    const { t } = useTranslation();
    // this is for the user collapse
    const [state, setState] = React.useState({});
    React.useEffect(() => {
      setState(getCollapseStates(routes));
    }, []);

    const getCollapseStates = (routes: any) => {
      let initialState = {};
      routes.map((prop: any) => {
        if (prop.collapse) {
          initialState = {
            [prop.state]: getCollapseInitialState(prop.children),
            ...getCollapseStates(prop.children),
            ...initialState
          };
        }
        return null;
      });
      return initialState;
    };

    const getCollapseInitialState = (routes: any) => {
      for (let i = 0; i < routes.length; i++) {
        if (routes[i].collapse && getCollapseInitialState(routes[i].children)) {
          return true;
        } else if (location.pathname === routes[i].path) {
          return true;
        }
      }
      return false;
    };

    const createNavLinks = (routes: any, protectedLink: any) => {
      return routes.map((prop: any, index: any) => {
        if (prop.redirect || (!prop.name) || (!protectedLink) && (!prop.protected)) {
          return null;
        } else if (prop.collapse) {
            let st: any = {};
            st[prop.state] = !(state as any)[prop.state];
            return (
            <Nav.Item
              className={getCollapseInitialState(prop.children) ? "active" : ""}
              as="li"
              key={index}
            >
              <Nav.Link
                className={(state as any)[prop.state] ? "nav-link": "nav-link collapsed"}
                data-toggle="collapse"
                onClick={(e:any) => {
                  e.preventDefault();
                  setState({ ...state, ...st });
                }}
                aria-expanded={(state as any)[prop.state]}
              >
                <div className="sb-nav-link-icon"><i className={prop.icon}></i></div>
                {t(prop.name)}
                <div className="sb-sidenav-collapse-arrow"><i className="fas fa-angle-down"></i></div>
              </Nav.Link>
              <Collapse in={(state as any)[prop.state]}>
                <div>
                  <Nav as="ul" className="sb-sidenav-menu-nested nav accordion">
                    {createNavLinks(prop.children,  (protectedLink || prop.protected))}
                  </Nav>
                </div>
              </Collapse>
          </Nav.Item>
        )} else {
        
        return (
          <Nav.Item
            className={activeRoute(prop.path)}
            key={index}
            as="li"
          >
            <Nav.Link to={prop.path} as={Link} className="nav-link collapsed">
              {prop.icon ? (
                <>
                  <i className={prop.icon} />
                  <p>{t(prop.name)}</p>
                </>
              ) : (
                <>
                  <p>{t(prop.name)}</p>
                </>
              )}
            </Nav.Link>
          </Nav.Item>
          );
        }
      });
    }

    const activeRoute = (path: any) => {
        return location.pathname === path ? "active" : "";
    };
  
    return (
        <>
            <nav className="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                <div className="sb-sidenav-menu">
                  <Nav as="ul" className="nav">
                    <div className="sb-sidenav-menu-heading">Camel Services</div>
                    {createNavLinks(routes, false)}
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
export default ProtectedSiteSidebar;