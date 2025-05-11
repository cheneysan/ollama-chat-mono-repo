import styled from 'styled-components'
import {Link, Outlet} from 'react-router'


const NavLinkContainer = styled.span`
    margin: 0 auto;
    font-size: 0.8rem;
`;

const NavLink = ({ to, children }) => {
    return (
        <NavLinkContainer>
            <Link to={to}>{children}</Link>
        </NavLinkContainer>
    )
};

export default NavLink;