export interface Channel {
  id: string
  name: string
  role: 'OWNER' | 'ADMIN' | 'GUEST'
}

export interface Friend {
  id: string
  username: string
}

export interface User {
  id: string
  username: string
  role: string
}

export interface Message {
  id: string
  sender: string
  content: string
  channelName: string
  timestamp: string
}
