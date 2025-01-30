import { useState } from 'react'

export const useCreateChannel = (fetchChannels: () => void) => {
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  const createChannel = async (channelName: string) => {
    setLoading(true)
    setError(null)

    try {
      const response = await fetch(
        `http://localhost:8080/channels/create?channelName=${encodeURIComponent(channelName)}`,
        {
          method: 'POST',
          credentials: 'include',
        },
      )

      if (!response.ok) {
        throw new Error('Failed to create channel')
      }

      // Refresh the list of channels after successful creation
      fetchChannels()
    } catch (error) {
      console.error('Error creating channel:', error)
      setError('Error creating channel')
    } finally {
      setLoading(false)
    }
  }

  return { createChannel, loading, error }
}
