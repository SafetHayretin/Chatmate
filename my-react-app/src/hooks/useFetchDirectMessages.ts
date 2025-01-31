import { useState } from 'react'
import { Message } from '../types/messeges.types'

export const useFetchDirectMessages = () => {
  const [messages, setMessages] = useState<Message[]>([])
  const [newMessage, setNewMessage] = useState<string>('')
  const [selectedFriend, setSelectedFriend] = useState<{ id: string; username: string } | null>(null)

  const fetchDirectMessages = async (friendId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/messages/direct?userId2=${encodeURIComponent(friendId)}`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error('Failed to fetch direct messages')
      }

      const data = await response.json()
      setMessages(data)
    } catch (error) {
      console.error('Error fetching direct messages:', error)
    }
  }

  const sendMessage = async () => {
    if (!newMessage.trim() || !selectedFriend) return

    try {
      const response = await fetch('http://localhost:8080/messages/send-direct', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ receiverId: selectedFriend.id, content: newMessage }),
      })

      if (!response.ok) {
        throw new Error('Failed to send direct message')
      }

      const sentMessage = await response.json()
      setMessages((prevMessages) => [...prevMessages, sentMessage])
      setNewMessage('')
    } catch (error) {
      console.error('Error sending direct message:', error)
    }
  }

  return {
    messages,
    newMessage,
    setNewMessage,
    selectedFriend,
    setSelectedFriend,
    fetchDirectMessages,
    sendMessage,
  }
}
