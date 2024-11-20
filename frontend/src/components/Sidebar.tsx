import { NavLink } from 'react-router-dom';
import { UserDetails } from '../context/AuthContext';

// I know this is terrible, no time to make it right tho
const sidebarRoutes = [
  {
    path: "/me/home",
    label: "Home",
    roles: ["ROLE_USER", "ROLE_ADMIN"],
  },
  {
    path: "/me/sensors",
    label: "Sensors",
    roles: ["ROLE_USER", "ROLE_ADMIN"], 
  },
  {
    path: "/admin/devices",
    label: "Devices",
    roles: ["ROLE_ADMIN"],
  },
  {
    path: "/admin/sensors",
    label: "Sensors",
    roles: ["ROLE_ADMIN"],
  },
  {
    path: "/admin/assign-devices",
    label: "Assign Devices",
    roles: ["ROLE_ADMIN"], 
  },
];
type SidebarProps = {
  isAuthenticated: boolean;
  user: UserDetails | null
}
const Sidebar = ({ isAuthenticated, user}: SidebarProps) => {
  const linkClasses = ({ isActive }: { isActive: boolean }): string =>
    `block p-2 rounded ${isActive ? 'font-bold' : 'hover:font-bold'}`;

  const visibleRoutes = sidebarRoutes.filter((route) =>
    route.roles.some((role) => user?.roles.includes(role))
  );

  return (
      <aside className="fixed pt-8 pl-4 top-16 h-screen w-36 shadow-sm z-50 bg-blue-400 text-white text-left">
      <nav>
        <ul>
            {visibleRoutes.map((route) => (
            <li key={route.path} className="mb-4">
              <NavLink to={route.path} className={linkClasses}>
                {route.label}
              </NavLink>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;