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
import KibanaEmbed from './pages/user/KibanaEmbed';

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
                element: 
                    <iframe 
                        src="http://localhost:5601/app/dashboards#/view/14b4f5b6-06c9-4e1c-97c7-25f1968d29fa?embed=true&_g=(refreshInterval%3A(pause%3A!t%2Cvalue%3A60000)%2Ctime%3A(from%3Anow-15m%2Cto%3Anow))&show-time-filter=true&hide-filter-bar=true"
                        style={{ width: "100%", height: "100%", border: "none" }} />
            },
            {
                path: "kibana-embed/:serialNumber",
                element: <KibanaEmbed />
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
