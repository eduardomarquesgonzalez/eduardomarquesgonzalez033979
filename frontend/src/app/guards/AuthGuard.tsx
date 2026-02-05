import { Navigate, Outlet } from 'react-router-dom'
import { useObservable } from '../../shared/hooks/useObservable'
import { authFacade } from '../../modules/auth/AuthFacade'

export default function AuthGuard() {
  const authState = useObservable(authFacade.getState(), authFacade.getCurrentState())

  if (!authState.isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}
