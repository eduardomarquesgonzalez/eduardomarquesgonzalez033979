import { httpService } from "@/shared/services/http";
import { authStore, User } from "../stores/auth.store";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  type: string;
  userId: number;
  username: string;
  role: string;
  expiresIn: number;
}

export interface RegisterRequest {
  username: string;
  password: string;
  role?: string;
}

class AuthService {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await httpService.post<LoginResponse>(
      "/auth/login",
      credentials,
    );

    if (response.token) {
      const user: User = {
        id: response.userId,
        username: response.username,
        role: response.role,
      };

      authStore.setAuth(user, response.token);

      if (response.refreshToken) {
        localStorage.setItem("refreshToken", response.refreshToken);
      }
    }

    return response;
  }

  async register(data: RegisterRequest): Promise<LoginResponse> {
    const response = await httpService.post<LoginResponse>(
      "/auth/register",
      data,
    );

    if (response.token) {
      const user: User = {
        id: response.userId,
        username: response.username,
        role: response.role,
      };

      authStore.setAuth(user, response.token);

      if (response.refreshToken) {
        localStorage.setItem("refreshToken", response.refreshToken);
      }
    }

    return response;
  }

  async getCurrentUser(): Promise<User> {
    const user = await httpService.get<User>("/auth/me");
    authStore.setUser(user);
    return user;
  }

  async refreshToken(): Promise<{ token: string; refreshToken: string }> {
    const refreshToken = localStorage.getItem("refreshToken");

    if (!refreshToken) {
      throw new Error("No refresh token available");
    }

    const response = await httpService.post<{
      token: string;
      refreshToken: string;
    }>("/auth/refresh", { refreshToken });

    if (response.token) {
      const currentState = authStore.getCurrentState();
      if (currentState.user) {
        authStore.setAuth(currentState.user, response.token);
      }

      if (response.refreshToken) {
        localStorage.setItem("refreshToken", response.refreshToken);
      }
    }

    return response;
  }

  logout(): void {
    authStore.clearAuth();
  }
}

export const authService = new AuthService();
