import { Box, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom"

const NotFoundPage = () => {
    const navigate = useNavigate();
    function handleGoHome() {
        navigate("/me/home");
    }

  return (
    <Box 
      display="flex" 
      flexDirection="column" 
      justifyContent="center"
      alignItems="center" 
      textAlign="center"
    >
      <Typography variant="h1" color="error">404</Typography>
      <Typography variant="h5" className="mt-4">Page Not Found</Typography>
      <Typography variant="body1" className="mt-2">
        The page you are looking for does not exist.
      </Typography>
      <Button
        variant="contained"
        color="primary"
        className="mt-6 bg-blue-600 hover:bg-blue-700"
        onClick={handleGoHome}
      >
        Go Home
      </Button>
    </Box>
  );
}

export default NotFoundPage