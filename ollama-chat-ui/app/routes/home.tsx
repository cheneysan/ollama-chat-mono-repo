// @ts-nocheck
import type { Route } from "./+types/home";
import { useAuth } from "../providers/auth-provider";
import {Navigate, useNavigate} from 'react-router'
import styled from "styled-components";
import Button from "../components/button";
import Input from '../components/input'
import Spinner from "../components/spinner";
import * as api from "../lib/api";
import {useEffect, useState } from "react";

export function meta({}: Route.MetaArgs) {
    return [
        { title: "OllamaChat" },
        { name: "description", content: "Chatting with Ollama!" },
    ];
}

const HomePageLayout = styled.div`
  display: flex;
  height: 100vh;
  background-image: radial-gradient(#343434, #141414);
  color: white; 
`;

const Sidebar = styled.div`
    min-width: 300px;
    width: 300px; 
    background-color: #282828;
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    overflow-y: auto; 
    border-right: 1px solid #444; 
`;

const ChatList = styled.div`
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    gap: 8px;
`;

const ChatItem = styled.button`
    padding: 10px;
    background-color: #3a3a3a; 
    border-radius: 5px;
    cursor: pointer;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    &:hover {
        background-color: #4a4a4a;
    }
`;

const MainContent = styled.div`
    flex-grow: 1;
    display: flex;
    flex-direction: column;
`;

const Header = styled.div`
    display: flex;
    gap: 10px;
    justify-content: flex-end;
    padding: 10px;
    background-color: #282828;
`;

const MessageArea = styled.div`
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    padding: 20px 30px;
`

const MessageInputArea = styled.div`
    display: flex;
    flex-direction: flex-row;
    padding: 10px;
    gap: 10px;
    background-color: #282828;
`;

const Message = styled.div`
    max-width: 75%;
    padding: 20px;
    border-radius: 8px;
    margin: 10px 10px 20px 10px;
`;

const UserMessage = styled(Message)`
    background-color: #494949;
    margin-right: auto;
`;

const BotMessage = styled(Message)`
    background-color: #003b90;
    margin-left: auto;
`;

const HeaderButton = styled.button`
    background-color: transparent;
    border: none;
    color: white;
    cursor: pointer;
    font-size: 0.8rem;
`;


export default function Home() {
    let [chat, setChat] = useState(null);
    let [loadingHistory, setLoadingHistory] = useState(false);
    let [loadingChat, setLoadingChat] = useState(false);
    let [waitingForResponse, setWaitingForResponse] = useState(false);
    let [history, setHistory] = useState([]);
    let [messages, setMessages] = useState([]);
    let [message, setMessage] = useState('');


    let { token, signout } = useAuth();
    if (!token) {
        return <Navigate to="/signin" replace/>;
    }

    useEffect(() => {
        const loadHistory = async () => {
            setLoadingHistory(true);
            try {
                let history = await api.getChatHistory(token);
                setHistory(history);
            } catch (error) {
                console.log(error);
            } finally {
                setLoadingHistory(false);
            }
        }

        loadHistory();
    }, []);

    const newChat = () => {
        setChat(null);
        setMessages([]);
        setMessage('');
    }

    const loadChat = async (chatId) => {
        if (chatId === null) {
            return;
        }

        try {
            setLoadingChat(true);
            let chat = await api.getChat(token, chatId);
            setChat(chat);
            setMessages(chat.messages);
        } catch (error) {
            console.log(error);
        } finally {
            setLoadingChat(false);
        }
    }

    const handleSendMessage = async () => {
        if (!message || message.trim().length === 0) {
            return;
        }

        console.log('Sending message: ' + message);

        // Clear the message and disable the input components
        setWaitingForResponse(true);

        // Create a new chat if we aren't in one
        let currentChat = chat;
        if (!currentChat) {
            let name = message.substring(0, 200);
            let newChat = await api.createNewChat(token, { name, message });
            setChat(newChat);
            setHistory([...history, newChat]);
            currentChat = newChat;
        }

        // Send the message
        // We optimistically add the message to the list. If the message fails to send,
        // rather than removing it again, we add the error as a new message from the server.
        try {
            // Optimistically add the message to the list
            setMessages(messages => [...messages, { id: messages.length + 1, text: message, sender: 'USER' }]);

            // Send to server and then add the response to the list as well
            let response = await api.sendMessage(token, {chatId: currentChat.id, message});
            setMessages(messages => [...messages, { id: messages.length + 1, text: response.text, sender: 'OLLAMA' }]);
        } catch (error) {
            console.log(error);
            // Add the error to the chat
            setMessages(messages => [...messages, { id: messages.length + 1, text: error.toString(), sender: 'OLLAMA' }]);

        } finally {
            setMessage('');
            setWaitingForResponse(false);
        }
    }

    return (
        <HomePageLayout>
            <Sidebar>
                <h3>History</h3>
                <ChatList>
                    {!loadingHistory && history.map(c => (
                        <ChatItem key={c.id} onClick={() => loadChat(c.id)}>
                            {c.title}
                        </ChatItem>
                    ))}
                    {loadingHistory && <Spinner/>}
                </ChatList>
            </Sidebar>
            <MainContent>
                <Header>
                    <HeaderButton onClick={() => newChat()}>New Chat</HeaderButton>
                    <HeaderButton onClick={() => signout()}>Sign Out</HeaderButton>
                </Header>
                <MessageArea>
                    {messages.map(message => {
                        if (message.sender === 'USER') {
                            return (<UserMessage key={message.id}>{message.text}</UserMessage>)
                        } else {
                            return (<BotMessage key={message.id}>{message.text}</BotMessage>)
                        }
                    })}
                </MessageArea>
                <MessageInputArea>
                    <Input
                        type="text"
                        placeholder="Type your message..."
                        disabled={waitingForResponse}
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        onKeyDown={async (e) => {
                            if (e.key === 'Enter') {
                                await handleSendMessage()
                            }
                        }}
                        style={{flexGrow: 1}}
                    />
                    <Button disabled={waitingForResponse} onClick={handleSendMessage}>
                        {waitingForResponse ? <Spinner/> : 'Send'}
                    </Button>
                </MessageInputArea>
            </MainContent>
        </HomePageLayout>
    );
}
