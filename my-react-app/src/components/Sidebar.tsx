import React, { useState } from 'react'
import { Channel, Friend } from '../types/messeges.types'
import { useSearchUsers } from '../hooks/useSearchUsers'
import { useAddFriend } from '../hooks/useAddFriend'
import { useFetchFriends } from '../hooks/useFetchFriends'

interface SidebarProps {
  channels: Channel[]
  friends: Friend[]
  onSelectChannel: (channel: Channel) => void
  onCreateChannel: (channelName: string) => void
}

const Sidebar: React.FC<SidebarProps> = ({ channels, friends, onSelectChannel }) => {
  const [] = useState('')
  const [isModalOpen, setIsModalOpen] = useState(false)

  const { searchUsers, searchResults, loading, error } = useSearchUsers()
  const [searchQuery, setSearchQuery] = useState('')
  const { sendFriendRequest, loading: addFriendLoading, error: addFriendError } = useAddFriend()
  const { fetchFriends } = useFetchFriends()

  const handleSearch = () => {
    searchUsers(searchQuery)
  }

  return (
    <div className="w-1/4 bg-gray-800 text-white p-4 flex flex-col justify-between">
      <div>
        <h2 className="text-xl font-bold mb-4">Списък с канали</h2>
        <ul>
          {channels.map((channel) => (
            <li
              key={channel.id}
              className="p-2 bg-gray-700 rounded mb-2 cursor-pointer hover:bg-gray-600"
              onClick={() => onSelectChannel(channel)}
            >
              {channel.name}
            </li>
          ))}
        </ul>
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
            <li key={friend.id} className="p-2 bg-gray-700 rounded">
              {friend.username}
            </li>
          ))}
        </ul>
      </div>

      {/* Search User Functionality */}
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
                onClick={() => sendFriendRequest(user.id, fetchFriends)}
                disabled={addFriendLoading}
              >
                {addFriendLoading ? 'Добавяне...' : 'Добави'}
              </button>
            </li>
          ))}
        </ul>
        {addFriendError && <p className="text-red-500 mt-2">{addFriendError}</p>}
      </div>
    </div>
  )
}

export default Sidebar
