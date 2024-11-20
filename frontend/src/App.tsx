import './App.css'
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AuthPage from './pages/AuthPage';
import { ProtectedRoute } from './components/ProtectedRoute';
import NotFoundPage from './pages/NotFoundPage';
import UnathorizedPage from './pages/UnathorizedPage';
import ResetPasswordPage from './pages/ResetPasswordPage';
import ChangePasswordPage from './pages/ChangePasswordPage';
import DevicesPage from './pages/admin/Devices/DevicesManager';
import SensorsPage from './pages/admin/Sensors/SensorsManager';
import AssignDeviceForm from './pages/admin/Devices/AssignDeviceForm';
import SensorsToKibana from './pages/user/SensorsToKibana';

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
        path: "/change-password-token",
        element: <ChangePasswordPage />
    },
    {
        path: "/me",
        element: <ProtectedRoute allowedRoles={["ROLE_USER", "ROLE_ADMIN"]} />,
        children:
        [
            {
                path: "home",
                element: <div>This is home</div>
            },
            {
                path: "sensors",
                element: <SensorsToKibana />
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
                element: <DevicesPage />
            },
            {
                path: "sensors",
                element: <SensorsPage />
            },
            {
                path: "assign-devices",
                element: <AssignDeviceForm />
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
