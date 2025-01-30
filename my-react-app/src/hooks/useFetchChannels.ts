import { useState, useEffect, useCallback } from 'react'
import { Channel } from '../types/messeges.types'

export const useFetchChannels = () => {
  const [channels, setChannels] = useState<Channel[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  const fetchChannels = useCallback(async () => {
    try {
      setIsLoading(true)
      const response = await fetch('http://localhost:8080/channels/user', {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error('Failed to fetch channels')
      }

      const data = await response.json()
      setChannels(data)
    } catch (err) {
      setError(err as Error)
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchChannels()
  }, [fetchChannels])

  return { channels, isLoading, error, refetch: fetchChannels }
}
