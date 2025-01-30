import { useState } from 'react'

export const useSearchUsers = () => {
  const [searchResults, setSearchResults] = useState<{ id: string; username: string }[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const searchUsers = async (query: string) => {
    if (!query) return

    setLoading(true)
    setError(null)

    try {
      const response = await fetch(`http://localhost:8080/user/search?query=${query}`, {
        credentials: 'include',
      })
      if (!response.ok) throw new Error('Потребителят не беше намерен')
      const data = await response.json()
      setSearchResults(data)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  return { searchUsers, searchResults, loading, error }
}
