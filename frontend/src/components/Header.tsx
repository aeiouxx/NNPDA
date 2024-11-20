import { UserDetails } from "../context/AuthContext";

type HeaderProps = {
  onLogout: () => void;
  isAuthenticated: boolean;
  user: UserDetails | null
}

const Header = ({ onLogout, isAuthenticated, user}: HeaderProps) => {
  if (!isAuthenticated) return null;

  return (
    <header className="flex justify-between items-center p-4 bg-blue-500 text-white fixed w-full top-0 z-50 h-16 shadow-lg">
      <h1 className="text-xl font-bold">Welcome, {user!.username}!</h1>
      <button onClick={onLogout} className="hover:bg-red-600 px-4 py-2 rounded font-bold">
        Logout
      </button>
    </header>
  );
};

export default Header