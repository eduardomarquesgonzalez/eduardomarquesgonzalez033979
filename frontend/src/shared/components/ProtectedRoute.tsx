import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/UseAuth";

interface ProtectedRouteProps {
  allowedRoles?: string[];
}

export const ProtectedRoute = ({ allowedRoles }: ProtectedRouteProps) => {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user?.role || "")) {
    return <Navigate to="/artists" replace />;
  }

  return <Outlet />;
};
