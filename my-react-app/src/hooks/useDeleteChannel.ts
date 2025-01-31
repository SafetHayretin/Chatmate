import { useState } from 'react'

export const useDeleteChannel = (fetchChannels: () => void) => {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const deleteChannel = async (channelId: string) => {
    if (!channelId) return

    const confirmDelete = window.confirm('Сигурни ли сте, че искате да изтриете този канал?')
    if (!confirmDelete) return

    setLoading(true)
    setError(null)

    try {
      const response = await fetch(`http://localhost:8080/channels/${channelId}`, {
        method: 'DELETE',
        credentials: 'include',
      })

      if (!response.ok) {
        const data = await response.json()
        throw new Error(data.message || 'Неуспешно изтриване на канал')
      }

      alert('Каналът беше изтрит успешно!')
      fetchChannels()
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return { deleteChannel, loading, error }
}
