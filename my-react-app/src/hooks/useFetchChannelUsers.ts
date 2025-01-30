import { useState } from 'react'

export const useFetchChannelUsers = () => {
  const [channelUsers, setChannelUsers] = useState<any[]>([])
  const [loading, setLoading] = useState<boolean>(false)
  const [error, setError] = useState<string | null>(null)

  const fetchChannelUsers = async (channelId: string) => {
    setLoading(true)
    setError(null)

    try {
      const response = await fetch(`http://localhost:8080/channels/${channelId}/users`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error('Failed to fetch channel users')
      }

      const data = await response.json()
      setChannelUsers(data)
    } catch (error) {
      console.error('Error fetching channel users:', error)
      setError('Error fetching channel users')
    } finally {
      setLoading(false)
    }
  }

  return { channelUsers, loading, error, fetchChannelUsers }
}
