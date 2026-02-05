import { useState, useEffect } from "react";
import { authStore, AuthState } from "@/modules/auth/stores/auth.store";

export function useAuth() {
  const [state, setState] = useState<AuthState>(authStore.getCurrentState());

  useEffect(() => {
    const subscription = authStore.getState().subscribe((newState) => {
      setState(newState);
    });

    return () => subscription.unsubscribe();
  }, []);

  const isAdmin = state.user?.role === "ADMIN";
  const isUser = state.user?.role === "USER";

  return {
    ...state,
    isAdmin,
    isUser,
    user: state.user,
  };
}
