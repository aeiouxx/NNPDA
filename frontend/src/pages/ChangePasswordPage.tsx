import { useForm } from "react-hook-form";
import { TextField, Button, Typography, Container, Paper, Box, Alert, CircularProgress } from "@mui/material";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import config from "../config";
import { mapErrorToMessage } from "../client/AxiosGetError";

const changePasswordSchema = z.object({
  token: z.string().min(1, "Token is required"),
  password: z.string()
    .min(8, "Password must be at least 8 characters")
});

interface ChangePasswordFormData {
  token: string;
  password: string;
}

const ChangePasswordPage = () => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  const { register, handleSubmit, formState: { errors } } = useForm<ChangePasswordFormData>({
    resolver: zodResolver(changePasswordSchema),
  });

  const onSubmit = async (data: ChangePasswordFormData) => {
    try {
      setIsSubmitting(true);
      setErrorMessage(null);
      setSuccessMessage(null);

      await axios.put(`${config.authBaseUrl}/change-password-token`, {
        token: data.token,
        password: data.password,
      });

      setSuccessMessage("Password has been successfully updated.");
      setTimeout(() => navigate("/auth"), 3000); // Redirect to login after success
    } catch (error: any) {
      const message = mapErrorToMessage(error, {
        400: "Invalid request. Please check your input.",
        401: "Invalid or expired token.",
      });
      setErrorMessage(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box
        position="absolute"
        top="50%"
        left="50%"
        style={{ transform: "translate(-50%, -50%)" }}
        width="100%"
        maxWidth="sm"
      >
        <Paper elevation={3} className="p-6 space-y-6">
          <Typography variant="h5" className="text-center font-bold">
            Change Password
          </Typography>
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
          {successMessage && <Alert severity="success">{successMessage}</Alert>}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <TextField
              label="Token"
              variant="outlined"
              fullWidth
              error={Boolean(errors.token)}
              helperText={errors.token?.message}
              {...register("token")}
              disabled={isSubmitting}
            />
            <TextField
              label="New Password"
              type="password"
              variant="outlined"
              fullWidth
              error={Boolean(errors.password)}
              helperText={errors.password?.message}
              {...register("password")}
              disabled={isSubmitting}
            />
            <Box position="relative">
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                className="mt-4 bg-blue-600 hover:bg-blue-700"
                disabled={isSubmitting}
              >
                {isSubmitting ? <CircularProgress size={24} /> : "Change Password"}
              </Button>
            </Box>
          </form>
        </Paper>
      </Box>
    </Container>
  );
};

export default ChangePasswordPage;