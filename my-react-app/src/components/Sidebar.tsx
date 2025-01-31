import React, { useState } from 'react'
import { Channel, Friend } from '../types/messeges.types'
import { useSearchUsers } from '../hooks/useSearchUsers'
import { useAddFriend } from '../hooks/useAddFriend'
import { useFetchFriends } from '../hooks/useFetchFriends'
import { useDeleteChannel } from '../hooks/useDeleteChannel'
import { useFetchChannels } from '../hooks/useFetchChannels'

interface SidebarProps {
  channels: Channel[]
  onSelectChannel: (channel: Channel | null) => void
  onCreateChannel: (channelName: string) => void
  onSelectFriend: (friend: Friend | null) => void
}

const Sidebar: React.FC<SidebarProps> = ({ channels, onSelectChannel, onCreateChannel, onSelectFriend }) => {
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [searchQuery, setSearchQuery] = useState('')
  const [newChannelName, setNewChannelName] = useState('')
  const { searchUsers, searchResults, loading, error } = useSearchUsers()
  const { sendFriendRequest, loading: addFriendLoading, error: addFriendError } = useAddFriend()
  const { friends, fetchFriends } = useFetchFriends()
  const { refetch } = useFetchChannels()
  const { deleteChannel, loading: deleteLoading, error: deleteError } = useDeleteChannel(refetch)

  const handleSearch = () => searchUsers(searchQuery)
  const handleCreateChannel = () => {
    if (newChannelName.trim()) {
      onCreateChannel(newChannelName)
      setNewChannelName('')
      setIsModalOpen(false)
    }
  }
  const handleSelectChannel = (channel: Channel) => {
    onSelectChannel(channel)
    onSelectFriend(null)
  }

  const handleSelectFriend = (friend: Friend) => {
    onSelectChannel(null)
    onSelectFriend(friend)
  }

  return (
    <div className="w-1/4 bg-gray-800 text-white p-4 flex flex-col justify-between">
      <div>
        <h2 className="text-xl font-bold mb-4">Списък с канали</h2>
        <ul>
          {channels.map((channel) => (
            <li key={channel.id} className="p-2 bg-gray-700 rounded mb-2 flex justify-between items-center">
              <span onClick={() => handleSelectChannel(channel)} className="cursor-pointer hover:text-gray-300">
                {channel.name}
              </span>
              {channel.role === 'OWNER' && (
                <button
                  onClick={() => deleteChannel(channel.id)}
                  className="bg-red-500 text-white p-1 rounded-md hover:bg-red-600 ml-2"
                  disabled={deleteLoading}
                >
                  🗑️
                </button>
              )}
            </li>
          ))}
        </ul>
        {deleteError && <p className="text-red-500 mt-2">{deleteError}</p>}
        <button
          onClick={() => setIsModalOpen(true)}
          className="w-full mt-4 bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600"
        >
          + Създай канал
        </button>
      </div>

      <div>
        <h2 className="text-xl font-bold mb-2">Списък с приятели</h2>
        <ul>
          {friends.map((friend) => (
            <li
              key={friend.id}
              className="p-2 bg-gray-700 rounded cursor-pointer hover:bg-gray-600"
              onClick={() => handleSelectFriend(friend)}
            >
              {friend.username}
            </li>
          ))}
        </ul>
      </div>

      <div className="mt-4">
        <h2 className="text-xl font-bold mb-2">Търсене на потребител</h2>
        <input
          type="text"
          placeholder="Въведи име..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full p-2 text-white border rounded-md"
        />
        <button
          onClick={handleSearch}
          className="w-full mt-2 bg-green-500 text-white p-2 rounded-md hover:bg-green-600"
        >
          Търси
        </button>
        {loading && <p className="text-gray-400 mt-2">Търсене...</p>}
        {error && <p className="text-red-500 mt-2">{error}</p>}
        <ul className="mt-2">
          {searchResults.map((user) => (
            <li key={user.id} className="p-2 bg-gray-700 rounded flex justify-between items-center">
              <span>{user.username}</span>
              <button
                className="bg-blue-500 text-white p-1 rounded-md hover:bg-blue-600"
                onClick={async () => {
                  await sendFriendRequest(user.id)
                  fetchFriends()
                }}
                disabled={addFriendLoading}
              >
                {addFriendLoading ? 'Добавяне...' : 'Добави'}
              </button>
            </li>
          ))}
        </ul>
        {addFriendError && <p className="text-red-500 mt-2">{addFriendError}</p>}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-6 rounded-md shadow-lg w-1/3">
            <h2 className="text-xl font-bold mb-4">Създай нов канал</h2>
            <input
              type="text"
              placeholder="Име на канал"
              value={newChannelName}
              onChange={(e) => setNewChannelName(e.target.value)}
              className="w-full p-2 text-black border rounded-md"
            />
            <div className="flex justify-end mt-4">
              <button
                onClick={() => setIsModalOpen(false)}
                className="mr-2 bg-gray-500 text-white p-2 rounded-md hover:bg-gray-600"
              >
                Отказ
              </button>
              <button onClick={handleCreateChannel} className="bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600">
                Създай
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Sidebar
