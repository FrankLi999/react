import MenuItems from './MenuItems';
import routes from "../../routes";
const TopNav = () => {
  return (
    <nav>
      <ul className="menus">
        {routes.map((route, index) => {
          const depthLevel = 0;
          
          return route.name ? (
            <MenuItems
              route={route}
              key={index}
              depthLevel={depthLevel}
            />
          ) : null;
        })}
      </ul>
    </nav>
  );
};

export default TopNav;
