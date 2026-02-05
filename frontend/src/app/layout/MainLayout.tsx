import { Outlet, Link, useNavigate, useLocation } from "react-router-dom";
import { authFacade } from "../../modules/auth/AuthFacade";
import { useObservable } from "../../shared/hooks/useObservable";
import Notification from "../../shared/components/Notification";

export default function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const authState = useObservable(
    authFacade.getState(),
    authFacade.getCurrentState(),
  );

  const handleLogout = () => {
    authFacade.logout();
    navigate("/login");
  };

  const isActive = (path: string) => location.pathname.startsWith(path);

  return (
    <div className="min-h-screen bg-spotiplag-black text-spotiplag-white flex">
      <Notification />

      {/* Sidebar */}
      <aside className="w-64 bg-spotiplag-black p-6 flex flex-col border-r border-spotiplag-darkgray">
        {/* Logo */}
        <Link to="/artists" className="mb-8">
          <div className="flex items-center gap-3">
            <svg
              className="w-10 h-10 text-spotiplag-blue"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z" />
            </svg>
            <span className="text-2xl font-bold">SpotiPLAG</span>
          </div>
        </Link>

        {/* Navigation */}
        <nav className="flex-1 space-y-2">
          <Link
            to="/artists"
            className={`flex items-center gap-4 px-4 py-3 rounded-md transition-colors ${
              isActive("/artists")
                ? "bg-spotiplag-lightgray text-spotiplag-white"
                : "text-spotiplag-lightwhite hover:text-spotiplag-white"
            }`}
          >
            <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z" />
            </svg>
            <span className="font-semibold">Artistas</span>
          </Link>

          <Link
            to="/albums"
            className={`flex items-center gap-4 px-4 py-3 rounded-md transition-colors ${
              isActive("/albums")
                ? "bg-spotiplag-lightgray text-spotiplag-white"
                : "text-spotiplag-lightwhite hover:text-spotiplag-white"
            }`}
          >
            <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 14.5c-2.49 0-4.5-2.01-4.5-4.5S9.51 7.5 12 7.5s4.5 2.01 4.5 4.5-2.01 4.5-4.5 4.5zm0-5.5c-.55 0-1 .45-1 1s.45 1 1 1 1-.45 1-1-.45-1-1-1z" />
            </svg>
            <span className="font-semibold">Álbuns</span>
          </Link>
        </nav>

        {/* User Section */}
        <div className="border-t border-spotiplag-darkgray pt-4">
          <div className="flex items-center justify-between px-4 py-3">
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-spotiplag-blue flex items-center justify-center">
                <span className="text-spotiplag-black font-bold text-sm">
                  {authState.user?.username?.charAt(0).toUpperCase()}
                </span>
              </div>
              <span className="text-sm font-semibold text-spotiplag-white truncate max-w-[120px]">
                {authState.user?.username}
              </span>
            </div>
            <button
              onClick={handleLogout}
              className="text-spotiplag-lightwhite hover:text-spotiplag-white transition-colors"
              title="Sair"
            >
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z" />
              </svg>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-y-auto bg-gradient-to-b from-spotiplag-darkgray to-spotiplag-black">
        <Outlet />
      </main>
    </div>
  );
}
