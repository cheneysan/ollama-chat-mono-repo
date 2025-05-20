// @ts-nocheck
import type { Route } from './+types/home'
import { useEffect, useState, Suspense } from 'react'
import { Navigate } from 'react-router'
import styled from 'styled-components'
import useSWR from 'swr';
import { useAuth } from '../providers/auth-provider'
import Sidebar from '../components/sidebar';
import ChatPanel from '../components/chat-panel';
import Spinner from '../components/spinner';
import { getChatHistory } from '../lib/api';

export function meta({ }: Route.MetaArgs) {
    return [
        { title: 'OllamaChat' },
        { name: 'description', content: 'Chatting with Ollama!' },
    ]
}

const HomePageLayout = styled.div`
    display: flex;
    height: 100vh;
    background-image: radial-gradient(#343434, #141414);
    color: white;
`

/**
 * Represents the Home component, which serves as the main layout for the application's homepage.
 * Handles user authentication and chat activation via state management and callback functions.
 *
 * @return {JSX.Element} The rendered Home component. If the user is not authenticated, redirects to the sign-in page.
 */
export default function Home() {
    let [activeChat, setActiveChat] = useState(null);
    let [chats, setChats] = useState([]);
    let { token, signout } = useAuth();

    if (!token) {
        return <Navigate to="/signin" replace />
    }

    let { data } = useSWR(`/api/chats/${token}`, () => getChatHistory(token), { suspense: true });
    useEffect(() => {
        setChats(data);
    }, [data]);

    const handleNewChat = (newChat) => {
        setChats((prevChats) => [...prevChats, newChat]);
        setActiveChat(newChat);
    };

    const handleActivateChat = (chat) => {
        setActiveChat(chat);
    }

    return (
        <HomePageLayout>
            <Sidebar chats={chats} onChatActivated={handleActivateChat} />
            <Suspense fallback={<Spinner />}>
                <ChatPanel onChatCreated={handleNewChat} onChatActivated={handleActivateChat} activeChat={activeChat} onSignout={signout} />
            </Suspense>
        </HomePageLayout>
    )
}
