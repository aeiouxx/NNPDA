import { FC } from "react";
import { useAuth, UserDetails } from "../context/AuthContext";
import { Navigate, useLocation, Outlet, useNavigate } from "react-router-dom";
import Header from "./Header";
import Sidebar from "./Sidebar";

interface ProtectedRouteProps {
  allowedRoles: string[];
}

export interface ProtectedContext {
  isAuthenticated: boolean;
  user: UserDetails | null;
}

export const ProtectedRoute: FC<ProtectedRouteProps> = ({ allowedRoles }) => {
  const { logout, isAuthenticated, user } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  console.log("Authenticated: ", isAuthenticated);

  if (!isAuthenticated) {
    // Redirect to login page if not authenticated
    return <Navigate to="/auth" state={{ from: location }} replace />;
  }

  if (user && allowedRoles.length > 0) {
    const hasRequiredRole = user.roles.some((role) => allowedRoles.includes(role));
    console.log("User roles: " + JSON.stringify(user.roles));
    if (!hasRequiredRole) {
      return <Navigate to="/unauthorized" replace />;
    }
  }

  const onLogout = () => {
    logout();
    navigate("/auth");
  };

  return (
    <div className="flex h-screen flex-col">
      <Header 
        isAuthenticated={isAuthenticated}
        onLogout={onLogout}
        user={user}
        />
      <div className="flex flex-1 flex-col mt-10 w-screen">
        <Sidebar 
          isAuthenticated={isAuthenticated}
          user={user} />
        <main id="page-wrapper"
          className="flex-1 ml-36">
            <Outlet context={{isAuthenticated, user}} />
          </main>
      </div>
    </div>
  );
};
