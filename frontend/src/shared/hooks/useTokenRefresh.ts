import { useEffect, useRef, useCallback } from 'react'
import { TokenUtils } from '../utils/token.utils'
import { authService } from '@/modules/auth/auth.service'
import { authStore } from '@/modules/auth/auth.store'

export function useTokenRefresh() {
  const timeoutRef = useRef<NodeJS.Timeout | null>(null)
  const lastTokenRef = useRef<string | null>(null)

  const scheduleTokenRefresh = useCallback(async (token: string) => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current)
    }

    const timeUntilExpiry = TokenUtils.getTimeUntilExpiry(token)
    
    const REFRESH_BEFORE_EXPIRY_MS = 60 * 1000 
    const refreshTime = Math.max(timeUntilExpiry - REFRESH_BEFORE_EXPIRY_MS, 0)

    if (refreshTime > 0) {
      const refreshTimeInSeconds = Math.round(refreshTime / 1000)
      console.log(`Token será renovado automaticamente em ${refreshTimeInSeconds} segundos`)
      
      timeoutRef.current = setTimeout(async () => {
        try {
          console.log('Iniciando renovação proativa do token...')
          
          const refreshToken = localStorage.getItem('refreshToken')
          if (!refreshToken) {
            console.error('Refresh token não encontrado')
            authService.logout()
            authStore.clearAuth()
            window.location.href = '/login'
            return
          }

          const response = await authService.refreshToken()
          
          localStorage.setItem('token', response.token)
          localStorage.setItem('refreshToken', response.refreshToken)

          const currentState = authStore.getCurrentState()
          if (currentState.user) {
            authStore.setAuth(currentState.user, response.token)
          }

          console.log('Token renovado com sucesso (proativo)')
          
          scheduleTokenRefresh(response.token)
        } catch (error) {
          console.error('Erro ao renovar token proativamente:', error)
          authService.logout()
          authStore.clearAuth()
          window.location.href = '/login'
        }
      }, refreshTime)
    } else {
      console.warn('Token já está próximo da expiração, renovando imediatamente...')
      
      try {
        const refreshToken = localStorage.getItem('refreshToken')
        if (!refreshToken) {
          throw new Error('No refresh token available')
        }

        const response = await authService.refreshToken()
        
        localStorage.setItem('token', response.token)
        localStorage.setItem('refreshToken', response.refreshToken)

        const currentState = authStore.getCurrentState()
        if (currentState.user) {
          authStore.setAuth(currentState.user, response.token)
        }

        console.log('Token renovado imediatamente')
        scheduleTokenRefresh(response.token)
      } catch (error) {
        console.error('Erro ao renovar token:', error)
        authService.logout()
        authStore.clearAuth()
        window.location.href = '/login'
      }
    }
  }, [])

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      return
    }
    if (token === lastTokenRef.current) {
      return
    }

    lastTokenRef.current = token

    if (TokenUtils.isTokenExpired(token)) {
      console.warn('Token expirado detectado, fazendo logout...')
      authService.logout()
      authStore.clearAuth()
      window.location.href = '/login'
      return
    }

    const tokenInfo = TokenUtils.getTokenInfo(token)
    console.log('Informações do token:')
    console.log(`  - Expira em: ${tokenInfo.expiresAt?.toLocaleString('pt-BR')}`)
    console.log(`  - Tempo restante: ${Math.round(tokenInfo.timeUntilExpiry / 1000)}s`)
    console.log(`  - Expirando em breve: ${tokenInfo.isExpiringSoon ? 'Sim' : 'Não'}`)

    scheduleTokenRefresh(token)

    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current)
      }
    }
  }, [scheduleTokenRefresh])

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current)
      }
    }
  }, [])
}