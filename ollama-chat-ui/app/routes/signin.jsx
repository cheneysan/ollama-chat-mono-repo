import {useForm} from 'react-hook-form';
import {useNavigate} from "react-router";
import {useState} from "react";
import Button from "../components/button";
import DialogContainer from "../components/dialog-container";
import ErrorMessage from "../components/error-message";
import FormContainer from "../components/form-container";
import Input from "../components/input";
import Title from "../components/title";
import Spinner from "../components/spinner.jsx";
import Navlink from "../components/navlink.jsx";
import {useAuth} from "../providers/auth-provider.jsx";
import {signin as apiSignin} from "../lib/api";

const SignUpForm = () => {
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [submitError, setSubmitError] = useState(null);
    const [busy, setBusy] = useState(false);
    const {signin} = useAuth();

    const nav = useNavigate();
    const query = new URLSearchParams(window.location.search);
    const message = query.get('message') ? atob(query.get('message')) : null;

    const onSubmit = async (data) => {
        try {
            setBusy(true);
            let token = await apiSignin(data);
            signin(token, () => nav('/'));
        } catch (error) {
            setSubmitError(error.message ? error.message : 'An unexpected error occurred. Try again later');
        } finally {
            setBusy(false);
        }
    };

    return (
        <DialogContainer>
            <FormContainer onSubmit={handleSubmit(onSubmit)} role="form" autoComplete="off">
                <Title>Sign In</Title>
                <center><p>{message ? message : 'Welcome back! Please sign in'}</p></center>
                <Input type="email"
                       placeholder="Email"
                       {...register('email', {
                           required: 'Email is required',
                           pattern: {value: /^\S+@\S+$/i, message: 'Invalid email address'}
                       })}
                />
                {errors.email && <ErrorMessage>{errors.email.message}</ErrorMessage>}

                <Input type="password"
                       placeholder="Password"
                       {...register("password", {
                           required: "Password is required",
                           minLength: {value: 8, message: 'Password must be at least 8 characters long'}
                       })}
                />
                {errors.password && <ErrorMessage>{errors.password.message}< /ErrorMessage>}

                <Button type="submit" disabled={busy}>
                    {busy ? <Spinner/> : 'Sign In'}
                </Button>
                {submitError && <ErrorMessage>{submitError}</ErrorMessage>}
                <Navlink to="/signup">Don't have an account? Register now!</Navlink>
            </FormContainer>
        </DialogContainer>
    )
};

export default SignUpForm;