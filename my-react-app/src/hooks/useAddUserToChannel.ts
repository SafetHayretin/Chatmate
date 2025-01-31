import { useState } from 'react'

interface UseAddUserToChannelProps {
  fetchChannelUsers: (channelId: string) => void
}

export const useAddUserToChannel = ({ fetchChannelUsers }: UseAddUserToChannelProps) => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const addUserToChannel = async (userId: string, selectedChannel: { id: string; name: string } | null) => {
    if (!selectedChannel) {
      alert('Изберете канал, преди да добавите приятел.')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await fetch('http://localhost:8080/channels/add-user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          channelId: selectedChannel.id,
          userId: userId,
          roleName: 'GUEST', // Default role
        }),
        credentials: 'include',
      })

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || 'Неуспешно добавяне на потребител')
      }

      alert(`Потребителят беше добавен в ${selectedChannel.name}!`)
      fetchChannelUsers(selectedChannel.id)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return { addUserToChannel, loading, error }
}
