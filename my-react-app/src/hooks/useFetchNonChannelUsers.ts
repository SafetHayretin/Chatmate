import { useState } from 'react'
import { User } from '../types/messeges.types'

export const useFetchNonChannelUsers = () => {
  const [nonChannelUsers, setNonChannelUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchNonChannelUsers = async (channelId: string) => {
    if (!channelId) return

    setLoading(true)
    setError(null)

    try {
      const response = await fetch(`http://localhost:8080/channels/not-added-users?channelId=${channelId}`, {
        credentials: 'include',
      })
      if (!response.ok) throw new Error('Неуспешно извличане на потребителите')
      const data = await response.json()
      setNonChannelUsers(data)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return { nonChannelUsers, fetchNonChannelUsers, loading, error }
}
