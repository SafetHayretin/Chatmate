import { useState, useEffect, useCallback } from 'react'
import { Channel } from '../types/messeges.types'

export const useFetchChannels = () => {
  const [channels, setChannels] = useState<Channel[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<Error | null>(null)

  const fetchChannels = useCallback(async () => {
    setIsLoading(true)
    setError(null)

    try {
      const response = await fetch('http://localhost:8080/channels/user', {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(errorText || 'Failed to fetch channels')
      }

      const data: Channel[] = await response.json()
      setChannels(data)
    } catch (err) {
      setError(err as Error)
    } finally {
      setIsLoading(false)
    }
  }, [setChannels, setIsLoading])

  useEffect(() => {
    fetchChannels()
  }, [fetchChannels])

  return { channels, isLoading, error, refetch: fetchChannels }
}
