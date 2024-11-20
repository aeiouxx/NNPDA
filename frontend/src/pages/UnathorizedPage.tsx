import { Box, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

const UnauthorizedPage = () => {
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
            height="100vh"
            width="100vw"
            textAlign="center"
        >
            <Typography variant="h1" color="error">
                403
            </Typography>
            <Typography variant="h5" className="mt-4">
                Unauthorized
            </Typography>
            <Typography variant="body1" className="mt-2">
                You do not have permission to access this page.
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
};

export default UnauthorizedPage;