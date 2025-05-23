import styled, {keyframes} from "styled-components";

const spin = keyframes`
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
`;

const SpinnerContainer = styled.div`
    border: 4px solid #f3f3f3;
    border-top: 4px solid #3498db;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    animation: ${spin} 1s linear infinite;
    margin: 0 auto;
`;

const Spinner = () => {
    return <SpinnerContainer data-testid="spinner" />;
};

export default Spinner;
