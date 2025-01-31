import { useState } from 'react'

export const useFetch = (url: string) => {
  const [data, setData] = useState<any>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = async (query: string) => {
    if (!query.trim()) return

    setIsLoading(true)
    setError(null)
    try {
      const response = await fetch(`${url}?query=${query}`)
      if (!response.ok) {
        throw new Error('Грешка при извличането на данни')
      }
      const result = await response.json()
      setData(result)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setIsLoading(false)
    }
  }

  return { data, isLoading, error, fetchData }
}

export const useUserSearch = () => {
  const {
    data: searchResults,
    isLoading,
    error,
    fetchData: searchUsers,
  } = useFetch('http://localhost:8080/user/search')
  return { searchResults: searchResults || [], isLoading, error, searchUsers }
}
