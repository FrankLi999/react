import { Nav } from 'react-bootstrap';
import MenuItems from './MenuItems';
const Dropdown = ({ routes, dropdown, depthLevel }) => {
  depthLevel = depthLevel + 1;
  const dropdownClass = depthLevel > 1 ? 'dropdown-submenu' : '';
  return (
    <Nav as="ul"
      className={`dropdown ${dropdownClass} ${
        dropdown ? 'show' : ''
      }`}
    >
      {routes.map((route, index) => (
        route.name && route.protected !== false ? (<MenuItems
          route={route}
          key={index}
          depthLevel={depthLevel}
        />) : null
      ))}
    </Nav>
  );
};

export default Dropdown;