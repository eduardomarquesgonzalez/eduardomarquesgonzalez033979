import { BehaviorSubject, Observable } from 'rxjs'
import { jwtDecode } from 'jwt-decode'
export interface User {
  id: number
  username: string
  role: string
}
export interface AuthState {
  isAuthenticated: boolean
  user: User | null
  token: string | null
  loading: boolean
}

const getInitialUser = (token: string | null): User | null => {
  if (!token) return null
  try {
    const decoded: any = jwtDecode(token)
    return {
      id: decoded.userId || decoded.id,
      username: decoded.sub || decoded.username,
      role: decoded.roles ? decoded.roles[0] : 'USER'
    }
  } catch (error) {
    console.error("Erro ao decodificar token inicial", error)
    localStorage.removeItem('token')
    return null
  }
}
class AuthStore {
  private initialToken = localStorage.getItem('token')
  
  private state = new BehaviorSubject<AuthState>({
    isAuthenticated: !!this.initialToken,
    user: getInitialUser(this.initialToken),
    token: this.initialToken,
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
    const finalUser = user.role ? user : (getInitialUser(token) || user)
    this.state.next({
      isAuthenticated: true,
      user: finalUser,
      token,
      loading: false,
    })
  }

  clearAuth() {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken') 
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