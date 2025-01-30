import React, { useEffect, useState } from 'react'
import Sidebar from './Sidebar'
import ChatWindow from './ChatWindow'
import { useFetchChannels } from '../hooks/useFetchChannels'
import { useUser } from '../contexts/UserContext'
import { useFetchFriends } from '../hooks/useFetchFriends'
import { useFetchMessages } from '../hooks/useFetchMessages'
import { useFetchChannelUsers } from '../hooks/useFetchChannelUsers'
import { useCreateChannel } from '../hooks/useCreateChannel'
import { useAddUserToChannel } from '../hooks/useAddUserToChannel'
import { useFetchNonChannelUsers } from '../hooks/useFetchNonChannelUsers'

const MainPage: React.FC = () => {
  const { user: userData, logout } = useUser()
  const { channels, refetch: fetchChannels } = useFetchChannels()
  const { friends } = useFetchFriends()
  const { messages, newMessage, setNewMessage, selectedChannel, setSelectedChannel, fetchMessages, sendMessage } =
    useFetchMessages()
  const { channelUsers, fetchChannelUsers } = useFetchChannelUsers()
  const [] = useState<string>('')
  const { createChannel } = useCreateChannel(fetchChannels)
  const { addUserToChannel, loading, error } = useAddUserToChannel({ fetchChannelUsers })
  const [isModalOpen, setIsModalOpen] = useState(false)
  const {
    nonChannelUsers,
    fetchNonChannelUsers,
    loading: nonChanelLoading,
    error: nonChanelError,
  } = useFetchNonChannelUsers()

  useEffect(() => {
    if (selectedChannel) {
      fetchNonChannelUsers(selectedChannel.id)
      fetchChannelUsers(selectedChannel.id)
    }
  }, [selectedChannel])

  return (
    <div className="flex h-screen">
      <Sidebar
        channels={channels}
        friends={friends}
        onSelectChannel={(ch) => {
          setSelectedChannel(ch)
          fetchMessages(ch.id)
        }}
        onCreateChannel={createChannel}
      />
      <div className="flex-1 flex flex-col">
        <div className="p-4 bg-gray-800 text-white flex justify-between items-center">
          <h2 className="text-xl font-bold">Добре дошъл, {userData?.username}</h2>
          <button onClick={logout} className="bg-red-500 px-4 py-2 rounded-md hover:bg-red-600">
            Изход
          </button>
        </div>

        <div className="flex flex-row">
          <ChatWindow
            selectedChannel={selectedChannel}
            messages={messages}
            newMessage={newMessage}
            setNewMessage={setNewMessage}
            sendMessage={sendMessage}
          />

          <div className="w-1/4 bg-gray-200 p-4 overflow-y-auto">
            <h3 className="text-lg font-bold">Членове на канала</h3>
            <ul>
              {channelUsers.length > 0 ? (
                channelUsers.map((user) => (
                  <li key={user.id} className="p-2 border-b flex justify-between">
                    <span>{user.username}</span>
                    <span className="text-gray-500 text-sm">{user.role}</span>
                  </li>
                ))
              ) : (
                <p className="text-gray-500">Няма членове</p>
              )}
            </ul>

            <button
              onClick={() => setIsModalOpen(true)}
              className="w-full mt-4 bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600"
            >
              Добави приятел в канала
            </button>
          </div>
        </div>
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md shadow-lg w-1/3">
            <h2 className="text-xl font-bold mb-4">Добави приятел в {selectedChannel?.name}</h2>

            {error && <p className="text-red-500">{nonChanelError}</p>}

            <ul>
              {nonChannelUsers.map((friend) => (
                <li key={friend.id} className="p-2 border-b flex justify-between">
                  <span>{friend.username}</span>
                  <button
                    onClick={() => addUserToChannel(friend.id, selectedChannel)}
                    className="bg-green-500 text-white p-1 rounded-md hover:bg-green-600"
                    disabled={nonChanelLoading}
                  >
                    {nonChanelLoading ? 'Добавяне...' : 'Добави'}
                  </button>
                </li>
              ))}
            </ul>

            <div className="flex justify-end mt-4">
              <button
                onClick={() => setIsModalOpen(false)}
                className="bg-gray-500 text-white p-2 rounded-md hover:bg-gray-600"
              >
                Затвори
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
export default MainPage
