import { useEffect, useRef, useCallback } from "react";
import { TokenUtils } from "../utils/token.utils";
import { authStore } from "@/modules/auth/stores/auth.store";
import { authService } from "@/modules/auth/services/auth.service";

export function useTokenRefresh() {
  const timeoutRef = useRef<number | null>(null);
  const lastTokenRef = useRef<string | null>(null);

  const handleLogout = useCallback(() => {
    authService.logout();
    authStore.clearAuth();
    window.location.href = "/login";
  }, []);

  const scheduleTokenRefresh = useCallback(
    async (token: string) => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }

      const timeUntilExpiry = TokenUtils.getTimeUntilExpiry(token);
      const REFRESH_BEFORE_EXPIRY_MS = 60 * 1000; // 1 minuto antes
      const refreshTime = Math.max(
        timeUntilExpiry - REFRESH_BEFORE_EXPIRY_MS,
        0,
      );

      if (refreshTime > 0) {
        console.log(
          `Token será renovado em ${Math.round(refreshTime / 1000)}s`,
        );

        timeoutRef.current = window.setTimeout(async () => {
          try {
            const response = await authService.refreshToken();
            const { token: newToken, refreshToken: newRefreshToken } = response;

            localStorage.setItem("token", newToken);
            localStorage.setItem("refreshToken", newRefreshToken);

            const { user } = authStore.getCurrentState();
            if (user) {
              authStore.setAuth(user, newToken);
            }

            scheduleTokenRefresh(newToken);
          } catch (error) {
            console.error("Falha na renovação automática:", error);
            handleLogout();
          }
        }, refreshTime);
      } else {
        try {
          const response = await authService.refreshToken();
          localStorage.setItem("token", response.token);
          localStorage.setItem("refreshToken", response.refreshToken);

          const { user } = authStore.getCurrentState();
          if (user) authStore.setAuth(user, response.token);

          scheduleTokenRefresh(response.token);
        } catch {
          handleLogout();
        }
      }
    },
    [handleLogout],
  );

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token || token === lastTokenRef.current) return;

    lastTokenRef.current = token;

    if (TokenUtils.isTokenExpired(token)) {
      handleLogout();
      return;
    }

    scheduleTokenRefresh(token);

    return () => {
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
    };
  }, [scheduleTokenRefresh, handleLogout]);
}
