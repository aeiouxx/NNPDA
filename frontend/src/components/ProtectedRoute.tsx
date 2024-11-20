import { FC } from "react";
import { useAuth } from "../context/AuthContext";
import { Navigate, useLocation, Outlet, useNavigate } from "react-router-dom";

interface ProtectedRouteProps {
  allowedRoles: string[];
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
      <Outlet />
    </div>
  );
};
