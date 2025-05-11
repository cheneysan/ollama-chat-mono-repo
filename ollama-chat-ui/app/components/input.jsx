import styled from 'styled-components'

const Input = styled.input`
    padding: 12px 15px;
    border: 1px solid #444;
    border-radius: 6px;
    background-color: #333;
    color: #eee;
    font-size: 1rem;

    &:focus {
        outline: none;
        border-color: cornflowerblue;
        box-shadow: 0 0 5px rgba(100, 149, 237, 0.5);
    }

    &::placeholder {
        color: #bbb;
    }
`;

export default Input;