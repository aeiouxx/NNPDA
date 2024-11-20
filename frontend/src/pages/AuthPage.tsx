import { useForm } from 'react-hook-form';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { TextField, Button, Typography, Container, Paper, Box, Alert, CircularProgress } from '@mui/material';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import config from '../config';
import axios from 'axios';
import { mapErrorToMessage } from '../client/AxiosGetError';

const loginSchema =   z.object({
  username: z.string().min(1, "Username is required"),
  password: z.string().min(1, "Password is required"),
});

const registerSchema = loginSchema.extend({
  email: z.string().min(1, "Email is required").email("Email is not valid"),
  confirmPassword: z.string().min(1, "Password confirmation is required")
}).superRefine((data, context) => {
  if (data.password !== data.confirmPassword) {
    context.addIssue({
      path: [...context.path, "confirmPassword"],
      code: z.ZodIssueCode.custom,
      message: "Passwords do not match"
    })
  }
});

interface AuthFormData {
  username: string;
  email?: string
  password: string;
  confirmPassword?: string;
}

const AuthPage = () => {
  const [isLogin, setIsLogin] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();
  const {login, isAuthenticated } = useAuth();
  const from = location.state?.from?.pathname || "/home";
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {register, handleSubmit, formState: {errors}} = useForm<AuthFormData>({
    resolver: zodResolver(isLogin ? loginSchema : registerSchema)
  });

  useEffect(() => {
    if (isAuthenticated) {
      navigate(from);
    }
  });

  const onSubmit = async (data : AuthFormData) => {
    console.log(data);
    console.log("Should redirect to " + from);
    console.log("Submitting request to: " + (isLogin ? "authenticate" : "register"));
    try {
      setIsSubmitting(true);
      const endpoint = isLogin ? "/login" : "/register";
      const body = isLogin 
        ? {username:data.username, password: data.password}
        : {username:data.username, email:data.email, password: data.password}

      const response = await axios.post(`${config.authBaseUrl}${endpoint}`, body);
      var token = response.data.token;
      login(token);
      navigate(from);
    } 
    catch (error : any) {
        var message = mapErrorToMessage(error,
        {
          409: "User already exists",
          401: "Invalid credentials",
          403: "Invalid credentials"
        });
        setErrorMessage(message);
    }
    finally{
      setIsSubmitting(false);
    }
  }
  const toggleAuthType = () => {
    setIsLogin(!isLogin);
    setErrorMessage(null);
  }
  return (
    <Container maxWidth="sm">
      <Box 
        position="absolute"
        top="50%"
        left="50%"
        style={{ transform: 'translate(-50%, -50%)' }}
        width="100%"
        maxWidth="sm"
      >
        <Paper elevation={3} className="p-6 space-y-6">
          <Typography variant="h5" className="text-center font-bold">
            {isLogin ? 'Login' : 'Register'}
          </Typography>
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <TextField
              label="Username"
              variant="outlined"
              fullWidth
              error={Boolean(errors.username)}
              helperText={errors.username?.message}
              {...register('username')}
              className="w-full"
              disabled={isSubmitting}
            />
            {!isLogin && (
              <TextField
                label="Email"
                variant="outlined"
                fullWidth
                error={Boolean(errors.email)}
                helperText={errors.email?.message}
                {...register('email')}
                className="w-full"
                disabled={isSubmitting}
              />
            )}
            <TextField
              label="Password"
              type="password"
              variant="outlined"
              fullWidth
              error={Boolean(errors.password)}
              helperText={errors.password?.message}
              {...register('password')}
              className="w-full"
              disabled={isSubmitting}
            />
            {!isLogin && (
              <TextField
                label="Confirm Password"
                type="password"
                variant="outlined"
                fullWidth
                error={Boolean(errors.confirmPassword)}
                helperText={errors.confirmPassword?.message}
                {...register('confirmPassword')}
                className="w-full"
                disabled={isSubmitting}
              />
            )}
            {isLogin && (
              <Typography variant="body2" className="text-center mt-2">
                <Link to="/reset-password" style={{ textDecoration: "none", color: "#1976d2" }}>
                  Forgot Password? Reset it here.
                </Link>
              </Typography>
            )}
            <Box position="relative">
              <Button 
                type="submit" 
                variant="contained" 
                color="primary" 
                fullWidth 
                className="mt-4 bg-blue-600 hover:bg-blue-700" 
                disabled={isSubmitting}
              >
                {isSubmitting ? <CircularProgress size={24} /> : (isLogin ? 'Log In' : 'Register')}
              </Button>
            </Box>
          </form>
          <Button onClick={toggleAuthType} variant="text" color="secondary" fullWidth disabled={isSubmitting}>
            {isLogin ? 'Need an account? Register' : 'Already have an account? Log In'}
          </Button>
        </Paper>
      </Box>
    </Container>
  );
}

export default AuthPage;