import { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react'
import { User } from '../types/messeges.types'
import { useNavigate } from 'react-router-dom'

interface UserContextType {
  user: User | null
  isLoading: boolean
  error: Error | null
  fetchUserData: () => void
  logout: () => void
}

const UserContext = createContext<UserContextType | undefined>(undefined)

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)
  const navigate = useNavigate()

  const fetchUserData = useCallback(async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://localhost:8080/user', { method: 'GET', credentials: 'include' })
      if (!response.ok) throw new Error('Not authenticated')

      const userData = await response.json()
      setUser(userData)
    } catch (err) {
      setError(err as Error)
      navigate('/login')
    } finally {
      setIsLoading(false)
    }
  }, [navigate])

  const logout = async () => {
    try {
      await fetch('http://localhost:8080/auth/logout', { method: 'POST', credentials: 'include' })
      setUser(null)
      navigate('/login')
    } catch (error) {
      console.error('Error logging out:', error)
    }
  }

  useEffect(() => {
    fetchUserData()
  }, [fetchUserData])

  return (
    <UserContext.Provider value={{ user, isLoading, error, fetchUserData, logout }}>{children}</UserContext.Provider>
  )
}

export const useUser = () => {
  const context = useContext(UserContext)
  if (!context) {
    throw new Error('useUser must be used within a UserProvider')
  }
  return context
}
