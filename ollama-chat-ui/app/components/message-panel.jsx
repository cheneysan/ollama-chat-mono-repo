import styled from "styled-components";
import PropTypes from "prop-types";

const MessageAreaContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 100%;
    overflow: scroll;
`;

const MessageArea = styled.div`
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
    padding: 20px 30px;
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


/**
 * Renders a message panel that displays user and bot messages within a styled container.
 *
 * @param {Object} props - The properties passed to the component.
 * @param {Array} props.messages - An array of message objects to display. Each message object should contain `id`, `text`, and `sender` properties. The `sender` determines whether the message is from the user or the bot.
 * @return {JSX.Element} A React component containing a styled message panel with messages categorized by sender.
 */
function MessagePanel({messages}) {
    return (
        <MessageAreaContainer>
            <MessageArea>
                {messages && messages.map(message => {
                    if (message.sender === 'USER') {
                        return (<UserMessage key={message.id}>{message.text}</UserMessage>)
                    } else {
                        return (<BotMessage key={message.id}>{message.text}</BotMessage>)
                    }
                })}
            </MessageArea>
        </MessageAreaContainer>
    );
}

MessagePanel.propTypes = {
    messages: PropTypes.array.optional,
};

export default MessagePanel;