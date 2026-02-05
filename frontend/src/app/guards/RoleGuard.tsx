import { useAuth } from "@/shared/hooks/UseAuth";
import { Navigate, Outlet } from "react-router-dom";

interface RoleGuardProps {
  allowedRoles: string[];
}

export default function RoleGuard({ allowedRoles }: RoleGuardProps) {
  const { isAuthenticated, user, loading } = useAuth();

  if (loading) {
    return null;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const hasAccess = user && allowedRoles.includes(user.role);

  return hasAccess ? <Outlet /> : <Navigate to="/artists" replace />;
}
