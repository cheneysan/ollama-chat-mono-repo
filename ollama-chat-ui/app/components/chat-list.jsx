import styled from "styled-components";
import PropTypes from "prop-types";

const ChatListContainer = styled.div`
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

/**
 * ChatList Component
 *
 * This component fetches and displays a list of previous chats made by
 * the authenticated user. It requires being wrapped in a `<Suspense>` boundary
 * to handle the loading state.
 *
 * @component
 * @param {Object} props - component props
 * @param {function} props.chats - the list of chats to display
 * @param {function} props.onChatActivated - callback handler used when the user clicks on a chat
 * @throws {Promise} throws a promise for `<Suspense>` to handle when fetching data
 *
 * @example
 * // Example usage
 * <Suspense fallback={<p>Loading...</p>}>
 *   <ChatList ... />
 * </Suspense>
 */
const ChatList = ({chats, onChatActivated}) => {
    return (
        <ChatListContainer>
            {chats.map(c => <ChatItem key={c.id} onClick={() => onChatActivated(c)}>{c.title}</ChatItem>)}
        </ChatListContainer>
    );
};

ChatList.propTypes = {
    chats: PropTypes.array.optional,
    onChatActivated: PropTypes.func.isRequired,
};

export default ChatList;