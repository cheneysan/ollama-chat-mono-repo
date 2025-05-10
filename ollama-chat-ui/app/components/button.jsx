import styled from "styled-components";

const Button = styled.button`
    padding: 12px 20px;
    background-color: cornflowerblue;
    color: white;
    border: none;
    border-radius: 6px; 
    cursor: pointer;
    font-size: 1.1rem;
    transition: background-color 0.3s ease; 

    &:hover {
        background-color: dodgerblue;
    }

    &:active {
        background-color: royalblue;
    }
`;

export default Button;