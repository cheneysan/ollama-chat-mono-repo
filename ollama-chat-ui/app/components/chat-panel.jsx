import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import styled from "styled-components";
import useSWR from "swr";
import * as api from "../lib/api.js";
import { useAuth } from "../providers/auth-provider.jsx";
import MessageInputPanel from "./message-input-panel.jsx";
import MessagePanel from "./message-panel.jsx";

const MainContent = styled.div`
    display: flex;
    flex-direction: column;
    width: 100%;
    max-height: 100vh;
    height: 100%;
`;

const Header = styled.div`
    display: flex;
    gap: 10px;
    justify-content: flex-end;
    padding: 10px;
    background-color: #282828;
`;

const HeaderButton = styled.button`
    background-color: transparent;
    border: none;
    color: white;
    cursor: pointer;
    font-size: 0.8rem;
`;

/**
 * ChatPanel is a functional component that handles rendering a chat interface, including message display,
 * input handling, and chat management for creating and activating chats.
 *
 * This component requires being wrapped in a `<Suspense>` boundary to handle the loading state.
 *
 * @param {Object} props - The properties passed to this component.
 * @param {Object|null} props.activeChat - The currently active chat object or null if no chat is active.
 * @param {Function} props.onChatCreated - A callback function to handle the creation of a new chat. It receives the newly created chat as an argument.
 * @param {Function} props.onChatActivated - A callback function to handle when a chat is activated. It receives the active chat or null to deactivate the current chat.
 * @param {Function} props.onSignout - A callback function to handle user sign-out.
 * @return {JSX.Element} The rendered JSX element representing the chat panel interface.
 */
function ChatPanel({ activeChat, onChatCreated, onChatActivated, onSignout }) {
    const { token } = useAuth();
    const [messages, setMessages] = useState([]);

    let { data } = useSWR(`/api/chat/${activeChat ? activeChat.id : null}/${token}`, () => {
        if (!activeChat) {
            return Promise.resolve({ messages: [] });
        } else {
            return api.getChat(token, activeChat.id);
        }
    }, { suspense: true });

    useEffect(() => {
        setMessages(data.messages);
    }, [data]);

    const addMessage = (message, sender) => {
        setMessages(messages => [...messages, { id: messages.length + 1, text: message, sender }]);
    };

    const sendMessage = async (chatId, message) => {
        try {
            addMessage(message, 'USER');
            let response = await api.sendMessage(token, { chatId, message });
            addMessage(response.text, 'OLLAMA');
        } catch (error) {
            console.log(error);
            addMessage(error.toString(), 'OLLAMA');
        }
    };

    const handleSendMessage = async (message) => {
        // If we don't have an active chat, create a new one with the message
        if (!activeChat) {
            let name = message.substring(0, 200);
            let chat = await api.createNewChat(token, { name, message });
            await sendMessage(chat.id, message);
            await onChatCreated(chat);
        }

        // Otherwise, send the message to the active chat
        else {
            await sendMessage(activeChat.id, message);
        }
    }

    return (
        <MainContent>
            <Header>
                <HeaderButton onClick={() => onChatActivated(null)}>New Chat</HeaderButton>
                <HeaderButton onClick={() => onSignout()}>Sign Out</HeaderButton>
            </Header>
            <MessagePanel messages={messages} />
            <MessageInputPanel onSendMessage={handleSendMessage} />
        </MainContent>
    );
}

ChatPanel.propTypes = {
    activeChat: PropTypes.object.optional,
    onChatCreated: PropTypes.func.isRequired,
    onChatActivated: PropTypes.func.isRequired,
    onSignout: PropTypes.func.isRequired,
};

export default ChatPanel;