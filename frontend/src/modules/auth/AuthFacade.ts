import { Observable } from 'rxjs'
import { authService, LoginRequest, RegisterRequest } from './auth.service'
import { authStore, AuthState, User } from './auth.store'

class AuthFacade {
  getState(): Observable<AuthState> {
    return authStore.getState()
  }

  getCurrentState(): AuthState {
    return authStore.getCurrentState()
  }

  async login(credentials: LoginRequest): Promise<void> {
    try {
      authStore.setLoading(true)
      const response = await authService.login(credentials)
      authStore.setAuth(response.user, response.token)
    } catch (error) {
      authStore.setLoading(false)
      throw error
    }
  }

  async register(data: RegisterRequest): Promise<void> {
    try {
      authStore.setLoading(true)
      const response = await authService.register(data)
      authStore.setAuth(response.user, response.token)
    } catch (error) {
      authStore.setLoading(false)
      throw error
    }
  }

  async loadCurrentUser(): Promise<void> {
    try {
      const user = await authService.getCurrentUser()
      authStore.setUser(user)
    } catch (error) {
      this.logout()
      throw error
    }
  }

  async refreshToken(): Promise<void> {
    try {
      const { token } = await authService.refreshToken()
      const currentState = authStore.getCurrentState()
      if (currentState.user) {
        authStore.setAuth(currentState.user, token)
      }
    } catch (error) {
      this.logout()
      throw error
    }
  }

  logout(): void {
    authService.logout()
    authStore.clearAuth()
  }

  isAuthenticated(): boolean {
    return authStore.getCurrentState().isAuthenticated
  }
}

export const authFacade = new AuthFacade()
