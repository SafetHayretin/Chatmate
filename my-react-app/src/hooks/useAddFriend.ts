import { useState } from 'react'

export const useAddFriend = () => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const sendFriendRequest = async (friendId: string) => {
    setLoading(true)
    setError(null)

    try {
      const response = await fetch('http://localhost:8080/friendships/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ friendId }),
      })

      if (!response.ok) throw new Error('Неуспешно добавяне на приятел')
      alert('Потребителят беше добавен успешно!')
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return { sendFriendRequest, loading, error }
}
