import { Navigate, Outlet } from "react-router-dom";
import { authStore } from "@/modules/auth/stores/auth.store";

export default function AuthGuard() {
  const { isAuthenticated } = authStore.getCurrentState();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}
