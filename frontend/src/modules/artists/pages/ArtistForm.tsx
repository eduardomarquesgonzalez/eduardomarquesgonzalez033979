import { useState, useEffect } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { useObservable } from "../../../shared/hooks/useObservable";
import artistFacade from "../ArtistFacade"; // Importação padrão sem chaves
import LoadingSpinner from "../../../shared/components/LoadingSpinner";

export default function ArtistForm() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditing = !!id;

  const {
    selectedArtist,
    loading,
    error: apiError,
  } = useObservable(artistFacade.getState(), artistFacade.getCurrentState());

  const [name, setName] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [localError, setLocalError] = useState<string | null>(null);

  useEffect(() => {
    if (isEditing && id) {
      artistFacade.loadArtistById(Number(id));
    }
    return () => {
      artistFacade.clearSelectedArtist();
    };
  }, [id, isEditing]);

  useEffect(() => {
    if (isEditing && selectedArtist) {
      setName(selectedArtist.name);
    }
  }, [selectedArtist, isEditing]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      setLocalError("O nome do artista é obrigatório.");
      return;
    }

    setSubmitting(true);
    setLocalError(null);

    try {
      if (isEditing && id) {
        await artistFacade.updateArtist(Number(id), { name });
        navigate(`/artists/${id}`);
      } else {
        const created = await artistFacade.createArtist({ name });
        navigate(`/artists/${created.id}`);
      }
    } catch (err) {
      console.error("Erro ao salvar:", err);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading && isEditing) return <LoadingSpinner />;

  return (
    <div className="p-8 max-w-2xl mx-auto">
      {/* Breadcrumb */}
      <nav className="mb-6 text-sm flex items-center gap-2 text-spotiplag-lightwhite">
        <Link to="/artists" className="hover:text-spotiplag-white">
          Artistas
        </Link>
        <span>/</span>
        <span className="text-spotiplag-white font-semibold">
          {isEditing ? "Editar Artista" : "Novo Artista"}
        </span>
      </nav>

      <h1 className="text-4xl font-bold text-spotiplag-white mb-8">
        {isEditing
          ? `Editar: ${selectedArtist?.name || ""}`
          : "Adicionar Novo Artista"}
      </h1>

      {(apiError || localError) && (
        <div className="bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-md mb-6">
          {apiError || localError}
        </div>
      )}

      <form
        onSubmit={handleSubmit}
        className="bg-spotiplag-gray p-8 rounded-lg shadow-xl"
      >
        <div className="mb-6">
          <label
            htmlFor="name"
            className="block text-sm font-bold text-spotiplag-white mb-2"
          >
            Nome do Artista *
          </label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full bg-spotiplag-darkgray border border-transparent focus:border-spotiplag-blue p-3 rounded text-white outline-none transition-all"
            placeholder="Ex: System of a Down"
            required
          />
        </div>

        <div className="flex gap-4">
          <button
            type="submit"
            disabled={submitting}
            className="flex-1 btn-primary disabled:opacity-50 font-bold py-3"
          >
            {submitting
              ? "Salvando..."
              : isEditing
                ? "Salvar Alterações"
                : "Criar Artista"}
          </button>
          <button
            type="button"
            onClick={() => navigate(-1)}
            className="flex-1 px-6 py-3 border border-spotiplag-lightwhite text-spotiplag-white rounded-full hover:border-white font-bold transition-all"
          >
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
}
