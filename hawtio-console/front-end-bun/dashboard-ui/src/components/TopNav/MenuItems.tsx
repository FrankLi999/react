import { useState, useEffect, useRef } from 'react';
import { Nav } from "react-bootstrap";
import Dropdown from './Dropdown';

import { Link } from 'react-router-dom';

const MenuItems = ({ route, depthLevel }) => {
  const [dropdown, setDropdown] = useState(false);

  let ref = useRef(null);

  useEffect(() => {
    const handler = (event) => {
      if (
        dropdown &&
        ref.current &&
        !(ref.current as HTMLLIElement).contains(event.target)
      ) {
        setDropdown(false);
      }
    };
    document.addEventListener('mousedown', handler);
    document.addEventListener('touchstart', handler);
    return () => {
      // Cleanup the event listener
      document.removeEventListener('mousedown', handler);
      document.removeEventListener('touchstart', handler);
    };
  }, [dropdown]);

  const onMouseEnter = () => {
    window.innerWidth > 960 && setDropdown(true);
  };

  const onMouseLeave = () => {
    window.innerWidth > 960 && setDropdown(false);
  };

  const closeDropdown = () => {
    dropdown && setDropdown(false);
  };

  return route.protectect === false ? null : (
    <Nav.Item as ="li"
      className="sb-menu-items"
      ref={ref}
      onMouseEnter={onMouseEnter}
      onMouseLeave={onMouseLeave}
      onClick={closeDropdown}
    >
      {route.path && route.children ? (
        <>
          <button
            type="button"
            aria-haspopup="menu"
            aria-expanded={dropdown ? 'true' : 'false'}
            onClick={() => setDropdown((prev) => !prev)}
          >
            {window.innerWidth < 960 && depthLevel === 0 ? (
              route.name
            ) : (
              <Nav.Link to={route.path} as={Link}>{route.name}</Nav.Link>
            )}

            {depthLevel > 0 &&
            window.innerWidth < 960 ? null : depthLevel > 0 &&
              window.innerWidth > 960 ? (
              <span>&raquo;</span>
            ) : (
              <span className="arrow" />
            )}
          </button>
          <Dropdown
            depthLevel={depthLevel}
            routes={route.children}
            dropdown={dropdown}
          />
        </>
      ) : !route.path && route.children ? (
        <>
          <button
            type="button"
            aria-haspopup="menu"
            aria-expanded={dropdown ? 'true' : 'false'}
            onClick={() => setDropdown((prev) => !prev)}
          >
            {route.name}{' '}
            {depthLevel > 0 ? (
              <span>&raquo;</span>
            ) : (
              <span className="arrow" />
            )}
          </button>
          <Dropdown
            depthLevel={depthLevel}
            routes={route.children.submenu}
            dropdown={dropdown}
          />
        </>
      ) : (
        <Nav.Link to={route.path} as={Link}>{route.name}</Nav.Link>
      )}
    </Nav.Item>
  );
};

export default MenuItems;