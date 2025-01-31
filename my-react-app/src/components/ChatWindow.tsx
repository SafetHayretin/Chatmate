import React, { useEffect, useRef, useState } from 'react'
import { Friend, Message } from '../types/messeges.types'
import { useRenameChannel } from '../hooks/useRenameChannel'

interface ChatWindowProps {
  selectedChannel: { id: string; name: string; role?: 'OWNER' | 'ADMIN' | 'GUEST' } | null
  selectedFriend: Friend | null
  messages: Message[]
  newMessage: string
  setNewMessage: (message: string) => void
  sendMessage: () => void
}

const ChatWindow: React.FC<ChatWindowProps> = ({
  selectedChannel,
  selectedFriend,
  messages,
  newMessage,
  setNewMessage,
  sendMessage,
}) => {
  const messagesEndRef = useRef<HTMLDivElement>(null)
  const { renameChannel, loading: renameLoading } = useRenameChannel()

  const [isRenaming, setIsRenaming] = useState(false)
  const [newChannelName, setNewChannelName] = useState(selectedChannel?.name || '')
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  useEffect(() => {
    if (selectedChannel) {
      setNewChannelName(selectedChannel.name)
    }
  }, [selectedChannel])
  const canRename = selectedChannel && (selectedChannel.role === 'OWNER' || selectedChannel.role === 'ADMIN')

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const handleRename = async () => {
    if (!selectedChannel || !newChannelName.trim()) return

    const updatedChannel = await renameChannel(selectedChannel.id, newChannelName)

    if (updatedChannel) {
      setIsRenaming(false)
    }
  }

  return (
    <div className="flex-1 bg-gray-100 p-6 flex flex-col">
      {selectedChannel || selectedFriend ? (
        <div>
          <div className="flex items-center">
            {isRenaming ? (
              <input
                type="text"
                value={newChannelName}
                onChange={(e) => setNewChannelName(e.target.value)}
                className="text-xl font-bold p-1 border border-gray-400 rounded"
              />
            ) : (
              <h2 className="text-2xl font-bold">
                {selectedChannel ? selectedChannel.name : `Чат с ${selectedFriend?.username}`}
              </h2>
            )}

            {canRename && !isRenaming && (
              <button
                onClick={() => setIsRenaming(true)}
                className="ml-2 bg-yellow-500 text-white p-1 rounded-md hover:bg-yellow-600"
              >
                ✏
              </button>
            )}

            {isRenaming && (
              <>
                <button
                  onClick={handleRename}
                  className="ml-2 bg-green-500 text-white p-1 rounded-md hover:bg-green-600"
                  disabled={renameLoading}
                >
                  ✔
                </button>
                <button onClick={() => setIsRenaming(false)} className="ml-2 text-red-500">
                  ✖
                </button>
              </>
            )}
          </div>
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
