import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authFacade } from "../AuthFacade";

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      await authFacade.login(formData);
      navigate("/artists");
    } catch (err: any) {
      const backendMessage = err.response?.data?.message;
      const details = err.response?.data?.details;
      const validationErrors = details
        ? Object.values(details).join(", ")
        : null;
      setError(
        validationErrors || backendMessage || "Erro ao conectar com o servidor",
      );
      console.error("Erro no login:", err.response?.data);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-spotiplag-darkgray to-spotiplag-black flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-12">
          <div className="flex items-center justify-center gap-3 mb-4">
            <svg
              className="w-16 h-16 text-spotiplag-blue"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M12 0C5.4 0 0 5.4 0 12s5.4 12 12 12 12-5.4 12-12S18.66 0 12 0zm5.521 17.34c-.24.359-.66.48-1.021.24-2.82-1.74-6.36-2.101-10.561-1.141-.418.122-.779-.179-.899-.539-.12-.421.18-.78.54-.9 4.56-1.021 8.52-.6 11.64 1.32.42.18.479.659.301 1.02zm1.44-3.3c-.301.42-.841.6-1.262.3-3.239-1.98-8.159-2.58-11.939-1.38-.479.12-1.02-.12-1.14-.6-.12-.48.12-1.021.6-1.141C9.6 9.9 15 10.561 18.72 12.84c.361.181.54.78.241 1.2zm.12-3.36C15.24 8.4 8.82 8.16 5.16 9.301c-.6.179-1.2-.181-1.38-.721-.18-.601.18-1.2.72-1.381 4.26-1.26 11.28-1.02 15.721 1.621.539.3.719 1.02.419 1.56-.299.421-1.02.599-1.559.3z" />
            </svg>
          </div>
          <h1 className="text-5xl font-bold text-spotiplag-white mb-2">
            SpotiPLAG
          </h1>
          <p className="text-spotiplag-lightwhite text-lg">
            Gerenciamento de Artistas e Álbuns
          </p>
        </div>

        {/* Login Form */}
        <div className="bg-spotiplag-black rounded-lg p-8 shadow-2xl">
          <h2 className="text-2xl font-bold text-spotiplag-white mb-6 text-center">
            Fazer login
          </h2>

          {error && (
            <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label
                htmlFor="username"
                className="block text-sm font-semibold text-spotiplag-white mb-2"
              >
                Usuário
              </label>
              <input
                type="text"
                id="username"
                value={formData.username}
                onChange={(e) =>
                  setFormData({ ...formData, username: e.target.value })
                }
                required
                className="input-spotiplag"
                placeholder="Digite seu usuário"
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-semibold text-spotiplag-white mb-2"
              >
                Senha
              </label>
              <input
                type="password"
                id="password"
                value={formData.password}
                onChange={(e) =>
                  setFormData({ ...formData, password: e.target.value })
                }
                required
                className="input-spotiplag"
                placeholder="Digite sua senha"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed mt-6"
            >
              {loading ? "Entrando..." : "Entrar"}
            </button>
          </form>

          <div className="mt-8 pt-6 border-t border-spotiplag-darkgray text-center">
            <p className="text-spotiplag-lightwhite text-sm">
              Não tem uma conta?{" "}
              <Link
                to="/register"
                className="text-spotiplag-blue hover:text-spotiplag-bluehover font-semibold"
              >
                Cadastre-se
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
