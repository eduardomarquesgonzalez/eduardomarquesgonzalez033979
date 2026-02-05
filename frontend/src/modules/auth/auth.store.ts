import { BehaviorSubject, Observable } from 'rxjs'

export interface AuthState {
  isAuthenticated: boolean
  user: User | null
  token: string | null
  loading: boolean
}

export interface User {
  id: number
  username: string
}

class AuthStore {
  private state = new BehaviorSubject<AuthState>({
    isAuthenticated: !!localStorage.getItem('token'),
    user: null,
    token: localStorage.getItem('token'),
    loading: false,
  })

  getState(): Observable<AuthState> {
    return this.state.asObservable()
  }

  getCurrentState(): AuthState {
    return this.state.value
  }

  setLoading(loading: boolean) {
    this.state.next({ ...this.state.value, loading })
  }

  setAuth(user: User, token: string) {
    localStorage.setItem('token', token)
    this.state.next({
      isAuthenticated: true,
      user,
      token,
      loading: false,
    })
  }

  clearAuth() {
    localStorage.removeItem('token')
    this.state.next({
      isAuthenticated: false,
      user: null,
      token: null,
      loading: false,
    })
  }

  setUser(user: User) {
    this.state.next({ ...this.state.value, user })
  }
}

export const authStore = new AuthStore()
