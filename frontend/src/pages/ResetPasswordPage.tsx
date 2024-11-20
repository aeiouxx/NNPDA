import { useForm } from "react-hook-form";
import { TextField, Button, Typography, Container, Paper, Box, Alert, CircularProgress } from "@mui/material";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useState } from "react";
import axios from "axios";
import config from "../config";
import { mapErrorToMessage } from "../utils/AxiosGetError";

const resetPasswordSchema = z.object({
  username: z.string().min(1, "Username is required"),
});

interface ResetPasswordFormData {
  username: string;
}

const ResetPasswordPage = () => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const { register, handleSubmit, formState: { errors } } = useForm<ResetPasswordFormData>({
    resolver: zodResolver(resetPasswordSchema),
  });

  const onSubmit = async (data: ResetPasswordFormData) => {
    try {
      setIsSubmitting(true);
      setErrorMessage(null);
      setSuccessMessage(null);

      await axios.post(`${config.apiBaseUrl}/user/password-reset-request`, { username: data.username });

      setSuccessMessage("Password reset request processed. If the username exists, further instructions will be sent.");
    } catch (error: any) {
      const message = mapErrorToMessage(error, {
        400: "Invalid request. Please check the username.",
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
            Reset Password
          </Typography>
          {errorMessage && <Alert severity="error">{errorMessage}</Alert>}
          {successMessage && <Alert severity="success">{successMessage}</Alert>}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <TextField
              label="Username"
              variant="outlined"
              fullWidth
              error={Boolean(errors.username)}
              helperText={errors.username?.message}
              {...register("username")}
              className="w-full"
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
                {isSubmitting ? <CircularProgress size={24} /> : "Request Password Reset"}
              </Button>
            </Box>
          </form>
        </Paper>
      </Box>
    </Container>
  );
};

export default ResetPasswordPage;
