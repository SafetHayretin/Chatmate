import { useState } from 'react'
import { Channel } from '../types/messeges.types'

export const useRenameChannel = () => {
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  const renameChannel = async (channelId: string, newName: string): Promise<Channel | null> => {
    setLoading(true)
    setError(null)

    try {
      const response = await fetch(`http://localhost:8080/channels/${channelId}/rename`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ newName }),
      })

      if (!response.ok) {
        throw new Error('Неуспешно преименуване на канала')
      }

      const updatedChannel = await response.json()
      return updatedChannel
    } catch (err) {
      setError('Неуспешно преименуване на канала')
      return null
    } finally {
      setLoading(false)
    }
  }

  return { renameChannel, loading, error }
}
