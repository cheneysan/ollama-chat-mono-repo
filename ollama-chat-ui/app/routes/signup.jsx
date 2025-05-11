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
import {registerNewUser} from "../lib/api";

const SignUpForm = () => {
    const {register, handleSubmit, watch, formState: {errors}} = useForm();
    const [submitError, setSubmitError] = useState(null);
    const [busy, setBusy] = useState(false);
    const password = watch("password");

    const nav = useNavigate();

    const onSubmit = async (data) => {
        const {passwordConfirm, ...formData} = data;

        setBusy(true);

        try {
            await registerNewUser(formData);
            const msg = 'Registration successful. Please sign in.'
            nav(`/signin?message=${btoa(msg)}`);
        } catch (error) {
            setSubmitError(error.message ? error.message : 'An unexpected error occurred. Try again later');
        } finally {
            setBusy(false);
        }
    };

    return (
        <DialogContainer>
            <FormContainer onSubmit={handleSubmit(onSubmit)} autoComplete="off">
                <Title>Register as New User</Title>
                <Input type="email"
                       placeholder="Email"
                       {...register('email', {
                           required: 'Email is required',
                           pattern: {value: /^\S+@\S+$/i, message: 'Invalid email address'}
                       })}
                />
                {errors.email && <ErrorMessage>{errors.email.message}</ErrorMessage>}

                <Input type="text"
                       placeholder="Display Name"
                       {...register("displayName", {
                           required: "Display name is required"
                       })}
                />
                {errors.displayName && <ErrorMessage>{errors.displayName.message}</ErrorMessage>}

                <Input type="password"
                       placeholder="Password"
                       {...register("password", {
                           required: "Password is required",
                           minLength: {value: 8, message: 'Password must be at least 8 characters long'}
                       })}
                />
                {errors.password && <ErrorMessage>{errors.password.message}< /ErrorMessage>}

                <Input type="password"
                       placeholder="Confirm Password"
                       {...register("passwordConfirm", {
                           required: "Please confirm your password",
                           validate: (value) => value === password || "Passwords do not match"
                       })}
                />
                {errors.passwordConfirm && <ErrorMessage>{errors.passwordConfirm.message}</ErrorMessage>}

                <Button type="submit" disabled={busy}>
                    {busy ? <Spinner/> : 'Register' }
                </Button>
                {submitError && <ErrorMessage>{submitError}</ErrorMessage>}
            </FormContainer>
        </DialogContainer>
    )
};

export default SignUpForm;