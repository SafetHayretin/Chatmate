import React, { useEffect, useRef } from 'react'
import { Message } from '../types/messeges.types'

interface ChatWindowProps {
  selectedChannel: { id: string; name: string } | null
  messages: Message[]
  newMessage: string
  setNewMessage: (message: string) => void
  sendMessage: () => void
}

const ChatWindow: React.FC<ChatWindowProps> = ({
  selectedChannel,
  messages,
  newMessage,
  setNewMessage,
  sendMessage,
}) => {
  const messagesEndRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  return (
    <div className="flex-1 bg-gray-100 p-6 flex flex-col">
      {selectedChannel ? (
        <div>
          <h2 className="text-2xl font-bold">{selectedChannel.name}</h2>
          <div className="mt-4 bg-white p-4 rounded-md shadow-md h-96 overflow-y-auto">
            {messages.length > 0 ? (
              messages.map((msg) => (
                <div key={msg.id} className="p-2 border-b">
                  <strong>{msg.sender}</strong>{' '}
                  <span className="text-gray-500 text-sm">{new Date(msg.timestamp).toLocaleString()}</span>
                  <p>{msg.content}</p>
                </div>
              ))
            ) : (
              <p className="text-gray-500">Няма съобщения</p>
            )}
            <div ref={messagesEndRef} />
          </div>
          <div className="mt-auto p-4 bg-white shadow flex">
            <input
              type="text"
              placeholder="Напиши съобщение..."
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              className="flex-1 p-2 border rounded-md"
            />
            <button
              onClick={sendMessage}
              className="ml-2 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600"
            >
              Изпрати
            </button>
          </div>
        </div>
      ) : (
        <h1 className="text-2xl font-bold">Изберете канал</h1>
      )}
    </div>
  )
}

export default ChatWindow
