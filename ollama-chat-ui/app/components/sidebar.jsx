import styled from "styled-components";
import ChatList from "./chat-list.jsx";
import PropTypes from "prop-types";

const SidebarContainer = styled.div`
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

/**
 * Sidebar component which renders the chat history for the authenticated user.
 *
 * @component
 * @param {Object} props - component props
 * @param {function} props.onChatActivated - callback function invoked when the user clicks a chat in the list
 */
const Sidebar = ({chats, onChatActivated}) => {
    return (
        <SidebarContainer data-testid="sidebar">
            <h3>History</h3>
            <ChatList chats={chats} onChatActivated={onChatActivated} />
        </SidebarContainer>
    );
}

Sidebar.propTypes = {
    chats: PropTypes.array.optional,
    onChatActivated: PropTypes.func.isRequired,
}

export default Sidebar;