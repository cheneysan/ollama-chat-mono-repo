import styled from "styled-components";
import Input from "./input.jsx";
import Button from "./button.jsx";
import Spinner from "./spinner.jsx";
import {useState, useTransition} from "react";
import PropTypes from "prop-types";

const MessageInputArea = styled.div`
    display: flex;
    flex-direction: row;
    padding: 10px;
    gap: 10px;
    background-color: #282828;
    margin-top: auto;
`;


/**
 * A functional component that represents a message input panel, allowing
 * users to type and send messages. Handles input state, message sending,
 * and disables input while awaiting a response.
 *
 * @param {Object} props - React component properties.
 * @param {Function} props.onSendMessage - A callback function triggered when a message is sent.
 * Accepts the message string as its argument and is expected to return a Promise.
 *
 * @return {JSX.Element} A JSX element rendering the message input panel, including
 * input and button components with appropriate behavior and state management.
 */
function MessageInputPanel({onSendMessage}) {
    const [message, setMessage] = useState('');
    const [waitingForResponse, startTransition] = useTransition();

    const handleSendMessage = async () => {
        if (!message || message.trim().length === 0) {
            return;
        }
        startTransition(async () => await onSendMessage(message));
        setMessage('');
    };

    return (
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
    )
}

MessageInputPanel.typeProps = {
    onSendMessage: PropTypes.func.isRequired,
};

export default MessageInputPanel;