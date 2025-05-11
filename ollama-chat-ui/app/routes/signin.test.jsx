import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import {describe, it, expect, vi} from 'vitest';
import SignInForm from './signin';
import {BrowserRouter as Router} from 'react-router';
import {useForm} from "react-hook-form";

// Mock react-hook-form
vi.mock('react-hook-form', () => ({
    useForm: () => ({
        register: vi.fn(),
        handleSubmit: vi.fn((fn) => fn),
        formState: {errors: {}},
    }),
}));

// Mock the useAuth hook
const mockSignIn = vi.fn();
vi.mock('../providers/auth-provider', () => ({
    useAuth: () => ({
        signin: mockSignIn,
    }),
    AuthProvider: ({children}) => <div>{children}</div>, // Simple AuthProvider mock
}));


// Mock fetch
global.fetch = vi.fn();

describe('SignInForm', () => {
    it('should render the sign in form', () => {
        render(<SignInForm/>, {wrapper: Router});
        expect(screen.getAllByText('Sign In')).toHaveLength(2); // Title and button
        expect(screen.getByPlaceholderText('Email')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
        expect(screen.getByRole('button', {name: 'Sign In'})).toBeInTheDocument();
        expect(screen.getByText("Don't have an account? Register now!")).toBeInTheDocument();
    });

    it('should show validation errors when submitting empty form', async () => {
        vi.mock('react-hook-form', () => ({
            useForm: () => ({
                register: vi.fn(),
                handleSubmit: vi.fn((fn) => fn),
                formState: {
                    errors: {
                        email: {type: 'required', message: 'Email is required'},
                        password: {type: 'required', message: 'Password is required'},
                    },
                },
            }),
        }));

        render(<SignInForm/>, {wrapper: Router});

        // fireEvent.click(screen.getByRole('button', {name: 'Sign In'}));
        const form = screen.getByRole('form'); // Get the form element
        fireEvent.submit(form);

        await waitFor(() => {
            expect(screen.getByText('Email is required')).toBeInTheDocument();
            expect(screen.getByText('Password is required')).toBeInTheDocument();
        });
    });

    it('should show spinner while busy', async () => {
        fetch.mockResolvedValueOnce({
            ok: true,
            json: () => new Promise(() => {}), // Never resolves to keep it busy
        });

        render(<SignInForm/>, {wrapper: Router});

        fireEvent.change(screen.getByPlaceholderText('Email'), {
            target: {value: 'test@example.com'},
        });

        fireEvent.change(screen.getByPlaceholderText('Password'), {
            target: {value: 'password123'},
        });

        const form = screen.getByRole('form');
        fireEvent.submit(form);

        await waitFor(() => {
            expect(screen.getByTestId('spinner')).toBeInTheDocument(); // Assuming Spinner component has data-testid="spinner"
        });
    });

    it('should display message from URL query parameter', () => {
        // Mock window.location.search
        Object.defineProperty(window, 'location', {
            value: {
                search: '?message=' + btoa('Welcome back!'),
            },
            writable: true,
        });

        render(<SignInForm/>, {wrapper: Router});

        expect(screen.getByText('Welcome back!')).toBeInTheDocument();
    });

    it('should display default message when no URL query parameter', () => {
        // Mock window.location.search
        Object.defineProperty(window, 'location', {
            value: {
                search: '',
            },
            writable: true,
        });

        render(<SignInForm/>, {wrapper: Router});

        expect(screen.getByText('Welcome back! Please sign in')).toBeInTheDocument();
    });
});