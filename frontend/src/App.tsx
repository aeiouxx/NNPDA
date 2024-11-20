import './App.css'
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AuthPage from './pages/AuthPage';
import { ProtectedRoute } from './components/ProtectedRoute';
import NotFoundPage from './pages/NotFoundPage';
import UnathorizedPage from './pages/UnathorizedPage';
import ResetPasswordPage from './pages/ResetPasswordPage';

const router = createBrowserRouter([
    {
        path: "/auth",
        element: <AuthPage />
    },
    {
        path: "/reset-password",
        element: <ResetPasswordPage />
    },
    {
        path: "/change-password",
        // element: <ChangePasswordPage />
    },
    {
        path: "/me",
        element: <ProtectedRoute allowedRoles={["ROLE_USER", "ROLE_ADMIN"]} />,
        children:
        [
            {
                path: "home",
                element: <div>This is home</div>
            }
        ]
    },
    {
        path: "/admin",
        element: <ProtectedRoute allowedRoles={["ROLE_ADMIN"]} />,
        children:
        [
            {
                path: "devices",
                element: <div>Admin devices</div>
            }
        ]
    },
    {
        path: "/unauthorized",
        element: <UnathorizedPage />
    },
    {
        path: "*",
        element: <NotFoundPage />
    }
]);

function App() {
    return (
        <AuthProvider>
            <RouterProvider router = {router}/>
        </AuthProvider>
    )
}

export default App;
