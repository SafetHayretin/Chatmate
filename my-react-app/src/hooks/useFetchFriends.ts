import { useState, useEffect, useCallback } from 'react'
import { User } from '../types/messeges.types'

export const useFetchFriends = () => {
  const [friends, setFriends] = useState<User[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  const fetchFriends = useCallback(async () => {
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/friendships/friends', {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error('Failed to fetch friends')
      }

      const data = await response.json()
      setFriends(data)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchFriends()
  }, [fetchFriends])

  return { friends, fetchFriends, loading, error }
}
