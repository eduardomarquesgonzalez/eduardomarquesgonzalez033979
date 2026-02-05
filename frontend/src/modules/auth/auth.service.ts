import { httpService } from "../../shared/services/http";
import { User } from "./auth.store";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  refreshToken: string;
  user: User;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

class AuthService {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await httpService.post<LoginResponse>(
      "/auth/login",
      credentials,
    );

    if (response.refreshToken) {
      localStorage.setItem("refreshToken", response.refreshToken);
    }

    return response;
  }

  async register(data: RegisterRequest): Promise<LoginResponse> {
    const response = await httpService.post<LoginResponse>(
      "/auth/register",
      data,
    );

    if (response.refreshToken) {
      localStorage.setItem("refreshToken", response.refreshToken);
    }

    return response;
  }

  async getCurrentUser(): Promise<User> {
    return httpService.get<User>("/auth/me");
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

    if (response.refreshToken) {
      localStorage.setItem("refreshToken", response.refreshToken);
    }

    return response;
  }

  logout(): void {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
  }
}

export const authService = new AuthService();
