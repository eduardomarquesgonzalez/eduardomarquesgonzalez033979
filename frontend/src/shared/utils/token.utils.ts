import { jwtDecode } from "jwt-decode";

interface JwtPayload {
  exp: number;
  sub: string;
  iat?: number;
}

export class TokenUtils {
  static isTokenExpired(token: string): boolean {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp < currentTime;
    } catch (error) {
      console.error("Erro ao decodificar token:", error);
      return true;
    }
  }

  static isTokenExpiringSoon(token: string, secondsBeforeExpiry = 60): boolean {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp < currentTime + secondsBeforeExpiry;
    } catch (error) {
      console.error("Erro ao decodificar token:", error);
      return true;
    }
  }

  static getTimeUntilExpiry(token: string): number {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const currentTime = Date.now() / 1000;
      const timeUntilExpiry = decoded.exp - currentTime;
      return timeUntilExpiry > 0 ? timeUntilExpiry * 1000 : 0;
    } catch (error) {
      console.error("Erro ao decodificar token:", error);
      return 0;
    }
  }

  static decodeToken(token: string): JwtPayload | null {
    try {
      return jwtDecode<JwtPayload>(token);
    } catch (error) {
      console.error("Erro ao decodificar token:", error);
      return null;
    }
  }

  static getTokenInfo(token: string): {
    isExpired: boolean;
    isExpiringSoon: boolean;
    timeUntilExpiry: number;
    expiresAt: Date | null;
  } {
    const decoded = this.decodeToken(token);

    return {
      isExpired: this.isTokenExpired(token),
      isExpiringSoon: this.isTokenExpiringSoon(token),
      timeUntilExpiry: this.getTimeUntilExpiry(token),
      expiresAt: decoded ? new Date(decoded.exp * 1000) : null,
    };
  }
}
