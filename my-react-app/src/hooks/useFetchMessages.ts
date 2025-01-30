import { useState } from 'react'
import { Message, Channel } from '../types/messeges.types'

export const useFetchMessages = () => {
  const [messages, setMessages] = useState<Message[]>([])
  const [newMessage, setNewMessage] = useState<string>('')
  const [selectedChannel, setSelectedChannel] = useState<Channel | null>(null)

  const fetchMessages = async (channelId: string) => {
    try {
      const response = await fetch(`http://localhost:8080/messages/channel/${channelId}`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error('Failed to fetch messages')
      }

      const data = await response.json()
      setMessages(data)
    } catch (error) {
      console.error('Error fetching messages:', error)
    }
  }

  const sendMessage = async () => {
    if (!newMessage.trim() || !selectedChannel) return

    try {
      const response = await fetch('http://localhost:8080/messages/send', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ channelId: selectedChannel.id, content: newMessage }),
      })

      if (!response.ok) {
        throw new Error('Failed to send message')
      }

      const sentMessage = await response.json()
      setMessages((prevMessages) => [...prevMessages, sentMessage])
      setNewMessage('')
    } catch (error) {
      console.error('Error sending message:', error)
    }
  }

  return {
    messages,
    newMessage,
    setNewMessage,
    selectedChannel,
    setSelectedChannel,
    fetchMessages,
    sendMessage,
  }
}
