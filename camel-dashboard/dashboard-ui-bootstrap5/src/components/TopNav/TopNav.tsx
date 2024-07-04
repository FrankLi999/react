import MenuItems from './MenuItems';
import routes from "../../routes";
import { Nav } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import './TopNav.css';
const TopNav = () => {
  return (
    <div className="sb-topmenu-area">
      <div className="sb-topmenu">
          <Link to="/" className="logo">
            Logo
          </Link>
        <nav>
          <Nav as="ul" className="sb-menus">
            {routes.map((route, index) => {
              const depthLevel = 0;
              
              return route.name && route.protected ? (
                <MenuItems
                  route={route}
                  key={index}
                  depthLevel={depthLevel}
                />
              ) : null;
            })}
          </Nav>
        </nav>
      </div>
    </div>
  );
};

export default TopNav;
